package com.itacademy.service.converters;

import com.itacademy.dao.entity.Concert;
import com.itacademy.dto.ConcertRead;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class ConcertToConcertReadConverter implements Converter<Concert, ConcertRead> {

  @Override
  public ConcertRead convert(Concert concert) {
    ConcertRead concertRead = new ConcertRead();

    concertRead.setUuid(concert.getUuid());
    concertRead.setDtCreate(concert.getDtCreate());
    concertRead.setDtUpdate(concert.getDtUpdate());
    concertRead.setTitle(concert.getTitle());
    concertRead.setDescription(concert.getDescription());
    concertRead.setDtEvent(concert.getDtEvent());
    concertRead.setDtEndOfSale(concert.getDtEndOfSale());
    concertRead.setType(concert.getEventType());
    concertRead.setStatus(concert.getEventStatus());

    return concertRead;
  }
}
