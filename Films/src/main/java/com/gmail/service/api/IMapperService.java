package com.gmail.service.api;

import com.gmail.dao.entity.Film;
import com.gmail.dto.CustomPage;
import com.gmail.dto.FilmCreateUpdate;
import com.gmail.dto.FilmRead;
import org.springframework.data.domain.Page;

public interface IMapperService {

  Film mapUpdateDtoToFilm(FilmCreateUpdate filmUpdate, Film filmFromDB);

  Film mapCreateDtoToFilm(FilmCreateUpdate filmCreate);

  FilmRead mapFilmToFilmRead(Film film);

  CustomPage<FilmRead> mapPage(Page<Film> page);

}
