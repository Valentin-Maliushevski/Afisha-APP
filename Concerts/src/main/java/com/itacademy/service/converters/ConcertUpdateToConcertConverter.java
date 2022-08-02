package com.itacademy.service.converters;

import com.itacademy.dao.entity.Concert;
import com.itacademy.dto.ConcertCreateUpdate;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

@Component
public class ConcertUpdateToConcertConverter {

  public Concert convert(ConcertCreateUpdate concertUpdate, Concert concertFromDB) {

    concertFromDB.setTitle(concertUpdate.getTitle());
    concertFromDB.setDescription(concertUpdate.getDescription());
    concertFromDB.setDtEvent(
        OffsetDateTime.ofInstant(Instant.ofEpochMilli(concertUpdate.getDt_event()), ZoneId.systemDefault()));
    concertFromDB.setDtEndOfSale(
        OffsetDateTime.ofInstant(Instant.ofEpochMilli(concertUpdate.getDt_end_of_sale()), ZoneId.systemDefault()));
    concertFromDB.setEventStatus(concertUpdate.getEventStatus());
    concertFromDB.setCategoryUuid(concertUpdate.getCategoryUuid());

    return concertFromDB;
  }
}
