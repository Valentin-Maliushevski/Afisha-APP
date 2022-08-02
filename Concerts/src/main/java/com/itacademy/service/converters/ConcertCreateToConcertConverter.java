package com.itacademy.service.converters;

import com.itacademy.dao.entity.Concert;
import com.itacademy.dto.ConcertCreateUpdate;
import com.itacademy.service.UserHolder;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ConcertCreateToConcertConverter implements Converter<ConcertCreateUpdate, Concert> {

  @Autowired
  UserHolder holder;

  @Override
  public Concert convert(ConcertCreateUpdate concertCreate) {
    Concert concert = new Concert();
    concert.setUuid(UUID.randomUUID());
    concert.setDtCreate(OffsetDateTime.now());
    concert.setDtUpdate(concert.getDtCreate());
    concert.setTitle(concertCreate.getTitle());
    concert.setDescription(concertCreate.getDescription());
    concert.setDtEvent(OffsetDateTime.ofInstant(Instant.ofEpochMilli(concertCreate.getDt_event()), ZoneId.systemDefault()));
    concert.setDtEndOfSale(OffsetDateTime.ofInstant(Instant.ofEpochMilli(concertCreate.getDt_end_of_sale()), ZoneId.systemDefault()));
    concert.setEventStatus(concertCreate.getEventStatus());
    concert.setEventType("concert");
    concert.setCategoryUuid(concertCreate.getCategoryUuid());
    concert.setAuthorUuid(holder.getUser().getUuid());

    return concert;
  }
}
