package com.gmail.service.api;

import com.gmail.dto.CustomPage;
import com.gmail.dto.FilmCreateUpdate;
import com.gmail.dao.entity.Film;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.util.UUID;
import javax.validation.Valid;

public interface IFilmService {

  void check(FilmCreateUpdate eventCreateUpdate) throws Multiple400Exception, SingleException;

  void add(@Valid FilmCreateUpdate eventCreateUpdate) throws Multiple400Exception, SingleException;

  void update(@Valid FilmCreateUpdate dto, UUID uuid, Long lastKnowUpdate)
      throws Multiple400Exception, SingleException;

  CustomPage<Film> getCustomPage(int page, int size) throws SingleException;

  Film getFilmByUuid(UUID uuid) throws SingleException;

//  Film getFilmByTitle(String title) throws SingleException;

}
