package com.itacademy.service.api;

import com.itacademy.dto.CustomPage;
import com.itacademy.dto.FilmCreateUpdate;
import com.itacademy.dto.FilmRead;
import com.itacademy.service.custom_exception.multiple.Multiple400Exception;
import java.util.UUID;
import javax.validation.Valid;

public interface IFilmService {

  void check(FilmCreateUpdate eventCreateUpdate) throws Multiple400Exception;

  void add(@Valid FilmCreateUpdate filmCreate) throws Multiple400Exception;

  void update(@Valid FilmCreateUpdate dto, UUID uuid, Long lastKnowUpdate)
      throws Multiple400Exception;

  CustomPage<FilmRead> getCustomPage(int page, int size);

  FilmRead getFilmByUuid(UUID uuid);

}
