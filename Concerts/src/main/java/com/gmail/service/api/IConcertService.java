package com.gmail.service.api;

import com.gmail.dao.entity.Concert;
import com.gmail.dto.CustomPage;
import com.gmail.dto.ConcertCreateUpdate;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.util.UUID;
import javax.validation.Valid;

public interface IConcertService {

  void check(ConcertCreateUpdate eventCreateUpdate) throws Multiple400Exception, SingleException;

  void add(@Valid ConcertCreateUpdate eventCreateUpdate)throws Multiple400Exception, SingleException;

  void update(@Valid ConcertCreateUpdate dto,
     UUID uuid, Long lastKnowUpdate)throws Multiple400Exception, SingleException;

  CustomPage<Concert> getCustomPage(int page, int size) throws SingleException;

  Concert getConcertByUuid(UUID uuid) throws SingleException;

//  Concert getConcertByTitle(String title) throws SingleException;

}
