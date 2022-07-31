package com.itacademy.service.converters;

import com.itacademy.dao.entity.Film;
import com.itacademy.dto.FilmRead;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class FilmToFilmReadConverter implements Converter<Film, FilmRead> {

  @Override
  public FilmRead convert(Film film) {
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
}
