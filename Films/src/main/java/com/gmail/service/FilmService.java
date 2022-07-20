package com.gmail.service;

import com.gmail.dto.CustomPage;
import com.gmail.dto.FilmCreateUpdate;
import com.gmail.dao.api.IFilmDao;
import com.gmail.dao.entity.Film;
import com.gmail.service.custom_exception.multiple.EachErrorDefinition;
import com.gmail.service.custom_exception.multiple.ErrorsDefinition;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
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
import com.gmail.service.api.IFilmService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Validated
public class FilmService implements IFilmService {

  private final IFilmDao filmDao;

  public FilmService(IFilmDao eventDao) {
    this.filmDao = eventDao;
  }

  @Override
  public void check(FilmCreateUpdate eventCreateUpdate) throws Multiple400Exception, SingleException {

//     final String titleFieldError = "Film with such title is already exist";
     final String dtEventFieldError = "Field must be later than now";
     final String dtEndOfSaleFieldError = "Field must be later than now and less than dt_event";
     final String realiseDateFieldError = "Field must contain the number of days since January 1, 1970";
     final String countryFieldError = "There are no such country in classifier";

     List<EachErrorDefinition> eachErrorDefinitions = new ArrayList<>();

//     if(getFilmByTitle(eventCreateUpdate.getTitle()) != null) {
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
       String url = "http://localhost/api/v1/classifier/country/" + eventCreateUpdate.getCountryUuid();
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
  public void add(@Valid FilmCreateUpdate eventCreateUpdate)
      throws Multiple400Exception, SingleException {

    check(eventCreateUpdate);

    Film film = new Film();
    film.setUuid(UUID.randomUUID());
    film.setDtCreate(OffsetDateTime.now());
    film.setDtUpdate(film.getDtCreate());
    film.setTitle(eventCreateUpdate.getTitle());
    film.setDescription(eventCreateUpdate.getDescription());
    film.setDtEvent(OffsetDateTime.ofInstant(Instant.ofEpochMilli(eventCreateUpdate.getDt_event()), ZoneId.systemDefault()));
    film.setDtEndOfSale(OffsetDateTime.ofInstant(Instant.ofEpochMilli(eventCreateUpdate.getDt_end_of_sale()), ZoneId.systemDefault()));
    film.setEventStatus(eventCreateUpdate.getEventStatus());
    film.setEventType("film");
    film.setCountryUuid(eventCreateUpdate.getCountryUuid());
    film.setDuration(eventCreateUpdate.getDuration());
    film.setReleaseYear(eventCreateUpdate.getReleaseYear());
    film.setReleaseDate(eventCreateUpdate.getReleaseDate());

    this.filmDao.save(film);
  }

  @Override
  public void update(@Valid FilmCreateUpdate eventCreateUpdate, UUID uuid, Long dtUpdate)
      throws Multiple400Exception, SingleException {

    check(eventCreateUpdate);

    Film film = this.getFilmByUuid(uuid);

    Long update = film.getDtUpdate().toInstant().toEpochMilli();

    if(!update.equals(dtUpdate)) {
      throw new SingleException();
    }

    film.setTitle(eventCreateUpdate.getTitle());
    film.setDescription(eventCreateUpdate.getDescription());
    film.setDtEvent(OffsetDateTime.ofInstant(Instant.ofEpochMilli(eventCreateUpdate.getDt_event()), ZoneId.systemDefault()));
    film.setDtEndOfSale(OffsetDateTime.ofInstant(Instant.ofEpochMilli(eventCreateUpdate.getDt_end_of_sale()), ZoneId.systemDefault()));
    film.setEventStatus(eventCreateUpdate.getEventStatus());
    film.setCountryUuid(eventCreateUpdate.getCountryUuid());
    film.setDuration(eventCreateUpdate.getDuration());
    film.setReleaseYear(eventCreateUpdate.getReleaseYear());
    film.setReleaseDate(eventCreateUpdate.getReleaseDate());

    this.filmDao.save(film);
  }

  @Override
  public CustomPage<Film> getCustomPage(int page, int size) throws SingleException {
    Pageable pageable = PageRequest.of(page, size, Sort.by("title"));

    Page page1 = this.filmDao.findAll(pageable);

    if (page + 1 > page1.getTotalPages()) {
      throw new SingleException();
    }

    CustomPage<Film> countriesPage = new CustomPage<>();
    countriesPage.setNumber(page);
    countriesPage.setSize(size);
    countriesPage.setTotalPages(page1.getTotalPages());
    countriesPage.setTotalElements(page1.getTotalElements());
    countriesPage.setNumberOfElements(page1.getContent().size());
    countriesPage.setFirstPage(page1.isFirst());
    countriesPage.setLastPage(page1.isLast());
    countriesPage.setContent(page1.getContent());

    return countriesPage;
  }

  @Override
  public Film getFilmByUuid(UUID uuid) throws SingleException {
    Film film = this.filmDao.findByUuid(uuid);
    if(film == null) {
      throw new SingleException();
    }
    return film;
  }

//  @Override
//  public Film getFilmByTitle(String title) throws SingleException {
//    if(title.isBlank()) {
//      throw new SingleException();
//    }
//    return this.filmDao.findByTitle(title);
//  }
}
