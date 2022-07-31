package com.itacademy.service;

import com.itacademy.dao.api.IFilmDao;
import com.itacademy.dao.entity.EventStatus;
import com.itacademy.dao.entity.Film;
import com.itacademy.dto.CustomPage;
import com.itacademy.dto.FilmCreateUpdate;
import com.itacademy.dto.FilmRead;
import com.itacademy.dto.user.User;
import com.itacademy.service.converters.FIlmUpdateToFilmConverter;
import com.itacademy.service.converters.FilmCreateToFilmConverter;
import com.itacademy.service.converters.FilmToFilmReadConverter;
import com.itacademy.service.converters.PageToCustomPageConverter;
import com.itacademy.service.custom_exception.multiple.EachErrorDefinition;
import com.itacademy.service.custom_exception.multiple.ErrorsDefinition;
import com.itacademy.service.custom_exception.multiple.Multiple400Exception;
import com.itacademy.service.custom_exception.single.SingleException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.itacademy.service.api.IFilmService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Validated
@Transactional(readOnly = true)
public class FilmService implements IFilmService {

  private final IFilmDao filmDao;
  private final UserHolder holder;
  private final FilmCreateToFilmConverter filmCreateToFilmConverter;
  private final FIlmUpdateToFilmConverter fIlmUpdateToFilmConverter;
  private final FilmToFilmReadConverter filmToFilmReadConverter;
  private final PageToCustomPageConverter pageToCustomPageConverter;

  public FilmService(IFilmDao filmDao, UserHolder holder,
      FilmCreateToFilmConverter filmCreateToFilmConverter,
      FIlmUpdateToFilmConverter fIlmUpdateToFilmConverter,
      FilmToFilmReadConverter filmToFilmReadConverter,
      PageToCustomPageConverter pageToCustomPageConverter) {
    this.filmDao = filmDao;
    this.holder = holder;
    this.filmCreateToFilmConverter = filmCreateToFilmConverter;
    this.fIlmUpdateToFilmConverter = fIlmUpdateToFilmConverter;
    this.filmToFilmReadConverter = filmToFilmReadConverter;
    this.pageToCustomPageConverter = pageToCustomPageConverter;
  }

  @Override
  public void check(FilmCreateUpdate eventCreateUpdate) throws Multiple400Exception {

     final String dtEventFieldError = "Field must be later than now";
     final String dtEndOfSaleFieldError = "Field must be later than now and less than dt_event";
     final String realiseDateFieldError = "Field must contain the number of days since January 1, 1970";
     final String countryFieldError = "There are no such country in classifier";

     List<EachErrorDefinition> eachErrorDefinitions = new ArrayList<>();

     if(eventCreateUpdate.getDt_event() < OffsetDateTime.now().toInstant().toEpochMilli()) {
       EachErrorDefinition errorDefinition = new EachErrorDefinition("dt_event", dtEventFieldError);
       eachErrorDefinitions.add(errorDefinition);
     }

     if(eventCreateUpdate.getDt_end_of_sale() > eventCreateUpdate.getDt_event()
         || eventCreateUpdate.getDt_end_of_sale() < OffsetDateTime.now().toInstant().toEpochMilli()){
       EachErrorDefinition errorDefinition = new EachErrorDefinition("dt_end_of_sale", dtEndOfSaleFieldError);
       eachErrorDefinitions.add(errorDefinition);
     }

     if(eventCreateUpdate.getReleaseDate().isBefore(LocalDate.parse("1970-01-01"))) {
       EachErrorDefinition errorDefinition = new EachErrorDefinition("release_date", realiseDateFieldError);
       eachErrorDefinitions.add(errorDefinition);
      }

     try{
       RestTemplate restTemplate = new RestTemplate();
       String url = "http://ClassifierService:80/api/v1/classifier/country/" + eventCreateUpdate.getCountryUuid();
       ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
     } catch (HttpClientErrorException e) {
       EachErrorDefinition errorDefinition = new EachErrorDefinition("country uuid", countryFieldError);
       eachErrorDefinitions.add(errorDefinition);
     }

    if(!eachErrorDefinitions.isEmpty()) {
      ErrorsDefinition errorsDefinition = new ErrorsDefinition(eachErrorDefinitions);
      throw new Multiple400Exception(errorsDefinition);
    }

  }

  @Override
  @Transactional
  public void add(@Valid FilmCreateUpdate filmCreate) throws Multiple400Exception {
    check(filmCreate);
    filmDao.save(filmCreateToFilmConverter.convert(filmCreate));
  }

  @Override
  @Transactional
  public void update(@Valid FilmCreateUpdate filmUpdate, UUID uuid, Long dtUpdate)
      throws Multiple400Exception {
    check(filmUpdate);

    Film film = filmDao.findByUuid(uuid);

    if(film == null) {
      throw new IllegalArgumentException("Film with such uuid is not found");
    }

    Long update = film.getDtUpdate().toInstant().toEpochMilli();

    if(!update.equals(dtUpdate) ) {
      throw new IllegalArgumentException("Update time is not valid");
    }

    if(film.getAuthorUuid().equals(holder.getUser().getUuid()) || holder.hasRoleAdmin()) {
      filmDao.save(fIlmUpdateToFilmConverter.convert(filmUpdate, film));
    } else {
      throw new IllegalArgumentException("User is not the author or is not Admin");
    }
  }

  @Override
  public CustomPage<FilmRead> getCustomPage(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
    if (holder.isAuthenticated()) {
      if (holder.hasRoleAdmin()) {
        Page page1 = filmDao.findAll(pageable);

        if (page + 1 > page1.getTotalPages()) {
          throw new IllegalArgumentException("There are less pages than you want");
        }
        return pageToCustomPageConverter.convert(page1);
      } else {
        User user = holder.getUser();

        Page page1 = filmDao.findByEventStatusOrAuthorUuid(EventStatus.PUBLISHED, user.getUuid(), pageable);

        if (page + 1 > page1.getTotalPages()) {
          throw new IllegalArgumentException("There are less pages than you want");
        }
        return pageToCustomPageConverter.convert(page1);
      }
    } else {

      Page page1 = filmDao.findByEventStatus(EventStatus.PUBLISHED, pageable);

      if (page + 1 > page1.getTotalPages()) {
        throw new IllegalArgumentException("There are less pages than you want");
      }
      return pageToCustomPageConverter.convert(page1);
    }
  }

  @Override
  public FilmRead getFilmByUuid(UUID uuid) {
    Film film = this.filmDao.findByUuid(uuid);
    if(film == null) {
      throw new IllegalArgumentException("User with such uuid is not found");
    }
    return filmToFilmReadConverter.convert(film);
  }
}

