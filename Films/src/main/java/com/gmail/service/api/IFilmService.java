package com.gmail.service.api;

import com.gmail.dto.CustomPage;
import com.gmail.dto.FilmCreateUpdate;
import com.gmail.dto.FilmRead;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;

public interface IFilmService {

  void check(FilmCreateUpdate eventCreateUpdate) throws Multiple400Exception, SingleException;

  void add(@Valid FilmCreateUpdate filmCreate) throws Multiple400Exception, SingleException;

  void update(@Valid FilmCreateUpdate dto, UUID uuid, Long lastKnowUpdate)
      throws Multiple400Exception, SingleException;

  CustomPage<FilmRead> getCustomPage(int page, int size) throws SingleException;

  FilmRead getFilmByUuid(UUID uuid) throws Multiple400Exception;

//  Film getFilmByTitle(String title) throws SingleException;

}
