package com.itacademy.service.converters;

import com.itacademy.dao.entity.Film;
import com.itacademy.dto.FilmCreateUpdate;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

@Component
public class FIlmUpdateToFilmConverter {

  public Film convert(FilmCreateUpdate filmUpdate, Film filmFromDB) {

    filmFromDB.setTitle(filmUpdate.getTitle());
    filmFromDB.setDescription(filmUpdate.getDescription());
    filmFromDB.setDtEvent(
        OffsetDateTime.ofInstant(Instant.ofEpochMilli(filmUpdate.getDt_event()), ZoneId.systemDefault()));
    filmFromDB.setDtEndOfSale(
        OffsetDateTime.ofInstant(Instant.ofEpochMilli(filmUpdate.getDt_end_of_sale()), ZoneId.systemDefault()));
    filmFromDB.setEventStatus(filmUpdate.getEventStatus());
    filmFromDB.setCountryUuid(filmUpdate.getCountryUuid());
    filmFromDB.setDuration(filmUpdate.getDuration());
    filmFromDB.setReleaseYear(filmUpdate.getReleaseYear());
    filmFromDB.setReleaseDate(filmUpdate.getReleaseDate());

    return filmFromDB;
  }
}
