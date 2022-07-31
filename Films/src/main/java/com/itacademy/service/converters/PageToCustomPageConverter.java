package com.itacademy.service.converters;

import com.itacademy.dao.entity.Film;
import com.itacademy.dto.CustomPage;
import com.itacademy.dto.FilmRead;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class PageToCustomPageConverter implements Converter<Page<Film>, CustomPage<FilmRead>> {

  private final FilmToFilmReadConverter filmToFilmReadConverter;

  public PageToCustomPageConverter(FilmToFilmReadConverter filmToFilmReadConverter) {
    this.filmToFilmReadConverter = filmToFilmReadConverter;
  }

  @Override
  public CustomPage<FilmRead> convert(Page<Film> page) {
    CustomPage<FilmRead> newPage = new CustomPage<>();
    List<FilmRead> filmsContent = new ArrayList<>();

    for(Film film : page.getContent()) {
      filmsContent.add(filmToFilmReadConverter.convert(film));
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
