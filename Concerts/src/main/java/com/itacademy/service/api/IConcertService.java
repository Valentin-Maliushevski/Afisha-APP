package com.itacademy.service.api;

import com.itacademy.service.custom_exception.multiple.Multiple400Exception;
import com.itacademy.dao.entity.Concert;
import com.itacademy.dto.ConcertRead;
import com.itacademy.dto.CustomPage;
import com.itacademy.dto.ConcertCreateUpdate;
import java.util.UUID;
import javax.validation.Valid;

public interface IConcertService {

  void check(ConcertCreateUpdate eventCreateUpdate) throws Multiple400Exception;

  void add(@Valid ConcertCreateUpdate eventCreateUpdate)throws Multiple400Exception;

  void update(@Valid ConcertCreateUpdate dto,
     UUID uuid, Long lastKnowUpdate)throws Multiple400Exception;

  CustomPage<Concert> getCustomPage(int page, int size);

  ConcertRead getConcertByUuid(UUID uuid);

}
