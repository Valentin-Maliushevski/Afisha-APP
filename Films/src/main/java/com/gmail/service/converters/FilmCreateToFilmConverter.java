package com.gmail.service.converters;

import com.gmail.dao.entity.Film;
import com.gmail.dto.FilmCreateUpdate;
import com.gmail.service.UserHolder;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class FilmCreateToFilmConverter implements Converter<FilmCreateUpdate, Film> {

  private final UserHolder holder;

  public FilmCreateToFilmConverter(UserHolder holder) {
    this.holder = holder;
  }

  @Override
  public Film convert(FilmCreateUpdate filmCreate) {
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
}
