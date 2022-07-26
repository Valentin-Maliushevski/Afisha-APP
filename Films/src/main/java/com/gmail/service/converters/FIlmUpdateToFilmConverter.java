package com.gmail.service.converters;

import com.gmail.dao.entity.Film;
import com.gmail.dto.FilmCreateUpdate;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Service;

@Service
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
