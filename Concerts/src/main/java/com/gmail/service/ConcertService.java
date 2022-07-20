package com.gmail.service;

import com.gmail.dao.entity.Concert;
import com.gmail.dto.CustomPage;
import com.gmail.dao.entity.EventStatus;
import com.gmail.dao.api.IConcertDao;
import com.gmail.dto.ConcertCreateUpdate;
import com.gmail.service.api.IConcertService;
import com.gmail.service.custom_exception.multiple.EachErrorDefinition;
import com.gmail.service.custom_exception.multiple.ErrorsDefinition;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Validated
public class ConcertService implements IConcertService {

  private final IConcertDao concertDao;

  public ConcertService(IConcertDao concertDao) {
    this.concertDao = concertDao;
  }

  @Override
  public void check(ConcertCreateUpdate eventCreateUpdate) throws Multiple400Exception, SingleException {

//     final String titleFieldError = "Concert with such title is already exist";
    final String dtEventFieldError = "Field must be later than now";
    final String dtEndOfSaleFieldError = "Field must be later than now and less than dt_event";
    final String categoryFieldError = "There are no such country in classifier";

    List<EachErrorDefinition> eachErrorDefinitions = new ArrayList<>();

//     if(getConcertByTitle(eventCreateUpdate.getTitle()) != null) {
//       EachErrorDefinition errorDefinition = new EachErrorDefinition("title", titleFieldError);
//       eachErrorDefinitions.add(errorDefinition);
//      }

    if(eventCreateUpdate.getDt_event() < OffsetDateTime.now().toInstant().toEpochMilli()) {
      EachErrorDefinition errorDefinition = new EachErrorDefinition("dt_event", dtEventFieldError);
      eachErrorDefinitions.add(errorDefinition);
    }

    if(eventCreateUpdate.getDt_end_of_sale() > eventCreateUpdate.getDt_event()
        || eventCreateUpdate.getDt_end_of_sale() < OffsetDateTime.now().toInstant().toEpochMilli()){
      EachErrorDefinition errorDefinition = new EachErrorDefinition("dt_end_of_sale", dtEndOfSaleFieldError);
      eachErrorDefinitions.add(errorDefinition);
    }

    try {
      RestTemplate restTemplate = new RestTemplate();
      String url = "http://localhost/api/v1/classifier/concert/category/" + eventCreateUpdate.getCategoryUuid();
      ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
    } catch (HttpClientErrorException e) {
      EachErrorDefinition errorDefinition = new EachErrorDefinition("category uuid", categoryFieldError);
      eachErrorDefinitions.add(errorDefinition);
    }

    if(!eachErrorDefinitions.isEmpty()) {
      ErrorsDefinition errorsDefinition = new ErrorsDefinition(eachErrorDefinitions);
      throw new Multiple400Exception(errorsDefinition);
    }
  }

  @Override
  public void add(@Valid ConcertCreateUpdate eventCreateUpdate)
      throws Multiple400Exception, SingleException {

    check(eventCreateUpdate);

    if(eventCreateUpdate.getTitle() == null || eventCreateUpdate.getDescription() == null
    || eventCreateUpdate.getDt_event() == null || eventCreateUpdate.getDt_end_of_sale() == null
    || eventCreateUpdate.getEventStatus() == null) {
      throw new IllegalArgumentException("Запрос содержит некорретные данные. Измените запрос и отправьте его ещё раз");
    }

    Concert concert = new Concert();
    concert.setUuid(UUID.randomUUID());
    concert.setDtCreate(OffsetDateTime.now());
    concert.setDtUpdate(concert.getDtCreate());
    concert.setTitle(eventCreateUpdate.getTitle());
    concert.setDescription(eventCreateUpdate.getDescription());
    concert.setDtEvent(OffsetDateTime.ofInstant(Instant.ofEpochMilli(eventCreateUpdate.getDt_event()), ZoneId.systemDefault()));
    concert.setDtEndOfSale(OffsetDateTime.ofInstant(Instant.ofEpochMilli(eventCreateUpdate.getDt_end_of_sale()), ZoneId.systemDefault()));
    concert.setEventStatus(EventStatus.valueOf(eventCreateUpdate.getEventStatus()));
    concert.setEventType("concert");
    concert.setCategoryUuid(eventCreateUpdate.getCategoryUuid());

    this.concertDao.save(concert);
  }

  @Override
  public void update(@Valid ConcertCreateUpdate eventCreateUpdate, UUID uuid, Long dtUpdate)
      throws Multiple400Exception, SingleException {

    check(eventCreateUpdate);

    if(eventCreateUpdate.getTitle() == null || eventCreateUpdate.getDescription() == null
        || eventCreateUpdate.getDt_event() == null || eventCreateUpdate.getDt_end_of_sale() == null
        || eventCreateUpdate.getEventStatus() == null) {
      throw new IllegalArgumentException("Запрос содержит некорретные данные.");
    }

    if(uuid == null || uuid.toString().isEmpty()) {
      throw new SingleException();
    }

    Concert concert = this.getConcertByUuid(uuid);

    Long update = concert.getDtUpdate().toInstant().toEpochMilli();

    if(!update.equals(dtUpdate) ) {
      throw new SingleException();
    }

    concert.setTitle(eventCreateUpdate.getTitle());
    concert.setDescription(eventCreateUpdate.getDescription());
    concert.setDtEvent(OffsetDateTime.ofInstant(Instant.ofEpochMilli(eventCreateUpdate.getDt_event()), ZoneId.systemDefault()));
    concert.setDtEndOfSale(OffsetDateTime.ofInstant(Instant.ofEpochMilli(eventCreateUpdate.getDt_end_of_sale()), ZoneId.systemDefault()));
    concert.setEventStatus(EventStatus.valueOf(eventCreateUpdate.getEventStatus()));
    concert.setCategoryUuid(eventCreateUpdate.getCategoryUuid());

    this.concertDao.save(concert);
  }

  @Override
  public CustomPage<Concert> getCustomPage(int page, int size) throws SingleException {
    Pageable pageable = PageRequest.of(page, size, Sort.by("title"));

    Page page1 = this.concertDao.findAll(pageable);

    if (page + 1 > page1.getTotalPages()) {
      throw new SingleException();
    }

    CustomPage<Concert> countriesPage = new CustomPage<>();
    countriesPage.setNumber(page);
    countriesPage.setSize(size);
    countriesPage.setTotalPages(page1.getTotalPages());
    countriesPage.setTotalElements(page1.getTotalElements());
    countriesPage.setNumberOfElements(page1.getContent().size());
    countriesPage.setFirstPage(page1.isFirst());
    countriesPage.setLastPage(page1.isLast());
    countriesPage.setContent(page1.getContent());

    return countriesPage;
  }

  @Override
  public Concert getConcertByUuid(UUID uuid) throws SingleException {
    Concert concert = this.concertDao.findByUuid(uuid);
    if(concert == null) {
      throw new SingleException();
    }
    return concert;
  }

//  @Override
//  public Film getFilmByTitle(String title) throws SingleException {
//    if(title.isBlank()) {
//      throw new SingleException();
//    }
//    return filmDao.findByTitle(title);
//  }

}
