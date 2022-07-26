package com.gmail.service;

import com.gmail.dao.entity.EventStatus;
import com.gmail.dto.CustomPage;
import com.gmail.dto.FilmCreateUpdate;
import com.gmail.dao.api.IFilmDao;
import com.gmail.dao.entity.Film;
import com.gmail.dto.FilmRead;
import com.gmail.dto.user.User;
import com.gmail.service.api.IMapperService;
import com.gmail.service.custom_exception.multiple.EachErrorDefinition;
import com.gmail.service.custom_exception.multiple.ErrorsDefinition;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.gmail.service.api.IFilmService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Validated
//@Transactional
public class FilmService implements IFilmService {

  private final IFilmDao filmDao;
  private final UserHolder holder;
  private final IMapperService mapperService;

  public FilmService(IFilmDao filmDao, UserHolder holder, IMapperService mapperService) {
    this.filmDao = filmDao;
    this.holder = holder;
    this.mapperService = mapperService;
  }

  @Override
  public void check(FilmCreateUpdate eventCreateUpdate) throws Multiple400Exception, SingleException {

//     final String titleFieldError = "Film with such title is already exist";
     final String dtEventFieldError = "Field must be later than now";
     final String dtEndOfSaleFieldError = "Field must be later than now and less than dt_event";
     final String realiseDateFieldError = "Field must contain the number of days since January 1, 1970";
     final String countryFieldError = "There are no such country in classifier";

     List<EachErrorDefinition> eachErrorDefinitions = new ArrayList<>();

//     if(this.filmDao.findByTitle(eventCreateUpdate.getTitle()) != null) {
//       EachErrorDefinition errorDefinition = new EachErrorDefinition("title", titleFieldError);
//       eachErrorDefinitions.add(errorDefinition);
//      }

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
       String url = "http://localhost:8082/api/v1/classifier/country/" + eventCreateUpdate.getCountryUuid();
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
  public void add(@Valid FilmCreateUpdate filmCreate)
      throws Multiple400Exception, SingleException {

    check(filmCreate);

    filmDao.save(mapperService.mapCreateDtoToFilm(filmCreate));
  }

  @Override
  public void update(@Valid FilmCreateUpdate filmUpdate, UUID uuid, Long dtUpdate)
      throws Multiple400Exception, SingleException {

    check(filmUpdate);

    Film film = filmDao.findByUuid(uuid);
    Long update = film.getDtUpdate().toInstant().toEpochMilli();

    if(!update.equals(dtUpdate) ) {
      throw new SingleException();
    }

    if(film.getAuthorUuid().equals(holder.getUser().getUuid()) || holder.hasRoleAdmin()) {
      this.filmDao.save( mapperService.mapUpdateDtoToFilm(filmUpdate, film));
    } else {
      throw new SingleException();
    }
  }

  @Override
  public CustomPage<FilmRead> getCustomPage(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
    if (holder.isAuthenticated()) {

      if (holder.hasRoleAdmin()) {
        return mapperService.mapPage(filmDao.findAll(pageable));
      } else {
        User user = holder.getUser();
        return mapperService.mapPage(filmDao.findByEventStatusOrAuthorUuid(EventStatus.PUBLISHED, user.getUuid(), pageable));
      }
    } else {
      return mapperService.mapPage(filmDao.findByEventStatus(EventStatus.PUBLISHED, pageable));
    }
  }

  @Override
  public FilmRead getFilmByUuid(UUID uuid) throws Multiple400Exception {
    Film film = this.filmDao.findByUuid(uuid);
    final String uuidError = "There are no film with such uuid";
    List<EachErrorDefinition> eachErrorDefinitions = new ArrayList<>();

    if(film == null) {
      EachErrorDefinition errorDefinition = new EachErrorDefinition("uuid", uuidError);
      eachErrorDefinitions.add(errorDefinition);
    }
    if(!eachErrorDefinitions.isEmpty()) {
      ErrorsDefinition errorsDefinition = new ErrorsDefinition(eachErrorDefinitions);
      throw new Multiple400Exception(errorsDefinition);
    }
    return mapperService.mapFilmToFilmRead(film);
  }

//  @Override
//  public Film getFilmByTitle(String title) throws SingleException {
//    if(title.isBlank()) {
//      throw new SingleException();
//    }
//    return this.filmDao.findByTitle(title);
//  }
}

