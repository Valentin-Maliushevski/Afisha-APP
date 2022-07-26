package com.gmail.service;

import com.gmail.dao.entity.Film;
import com.gmail.dto.CustomPage;
import com.gmail.dto.FilmCreateUpdate;
import com.gmail.dto.FilmRead;
import com.gmail.service.api.IMapperService;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class MapperService implements IMapperService {

  private final UserHolder holder;

  public MapperService(UserHolder holder) {
    this.holder = holder;
  }

  @Override
  public Film mapUpdateDtoToFilm(FilmCreateUpdate filmUpdate, Film filmFromDB) {

    filmFromDB.setTitle(filmUpdate.getTitle());
    filmFromDB.setDescription(filmUpdate.getDescription());
    filmFromDB.setDtEvent(OffsetDateTime.ofInstant(Instant.ofEpochMilli(filmUpdate.getDt_event()), ZoneId.systemDefault()));
    filmFromDB.setDtEndOfSale(OffsetDateTime.ofInstant(Instant.ofEpochMilli(filmUpdate.getDt_end_of_sale()), ZoneId.systemDefault()));
    filmFromDB.setEventStatus(filmUpdate.getEventStatus());
    filmFromDB.setCountryUuid(filmUpdate.getCountryUuid());
    filmFromDB.setDuration(filmUpdate.getDuration());
    filmFromDB.setReleaseYear(filmUpdate.getReleaseYear());
    filmFromDB.setReleaseDate(filmUpdate.getReleaseDate());

    return filmFromDB;
  }

  @Override
  public Film mapCreateDtoToFilm(FilmCreateUpdate filmCreate) {
    Film film = new Film();

    film.setUuid(UUID.randomUUID());
    film.setDtCreate(OffsetDateTime.now());
    film.setDtUpdate(film.getDtCreate());
    film.setTitle(filmCreate.getTitle());
    film.setDescription(filmCreate.getDescription());
    film.setDtEvent(OffsetDateTime.ofInstant(Instant.ofEpochMilli(filmCreate.getDt_event()), ZoneId.systemDefault()));
    film.setDtEndOfSale(OffsetDateTime.ofInstant(Instant.ofEpochMilli(filmCreate.getDt_end_of_sale()), ZoneId.systemDefault()));
    film.setEventStatus(filmCreate.getEventStatus());
    film.setEventType("film");
    film.setCountryUuid(filmCreate.getCountryUuid());
    film.setDuration(filmCreate.getDuration());
    film.setReleaseYear(filmCreate.getReleaseYear());
    film.setReleaseDate(filmCreate.getReleaseDate());
    film.setAuthorUuid(holder.getUser().getUuid());

    return film;
  }

  @Override
  public FilmRead mapFilmToFilmRead(Film film) {
    FilmRead filmRead = new FilmRead();

    filmRead.setUuid(film.getUuid());
    filmRead.setDtCreate(film.getDtCreate());
    filmRead.setDtUpdate(film.getDtUpdate());
    filmRead.setTitle(film.getTitle());
    filmRead.setDescription(film.getDescription());
    filmRead.setDtEvent(film.getDtEvent());
    filmRead.setDtEndOfSale(film.getDtEndOfSale());
    filmRead.setType(film.getEventType());
    filmRead.setStatus(film.getEventStatus());

    return filmRead;
  }

  @Override
  public CustomPage<FilmRead> mapPage(Page<Film> page) {

    CustomPage<FilmRead> newPage = new CustomPage<>();
    List<FilmRead> filmsContent = new ArrayList<>();

    for(Film film : page.getContent()) {
      filmsContent.add(mapFilmToFilmRead(film));
    }

    newPage.setNumber(page.getNumber());
    newPage.setSize(page.getSize());
    newPage.setTotalPages(page.getTotalPages());
    newPage.setTotalElements(page.getTotalElements());
    newPage.setNumberOfElements(page.getNumberOfElements());
    newPage.setFirstPage(page.isFirst());
    newPage.setLastPage(page.isLast());
    newPage.setContent(filmsContent);

    return newPage;
  }
}
