package com.itacademy.service;

import com.itacademy.service.api.IConcertService;
import com.itacademy.service.converters.ConcertCreateToConcertConverter;
import com.itacademy.service.converters.ConcertUpdateToConcertConverter;
import com.itacademy.service.custom_exception.multiple.EachErrorDefinition;
import com.itacademy.service.custom_exception.multiple.Multiple400Exception;
import com.itacademy.dao.entity.Concert;
import com.itacademy.dao.entity.EventStatus;
import com.itacademy.dto.ConcertRead;
import com.itacademy.dto.CustomPage;
import com.itacademy.dao.api.IConcertDao;
import com.itacademy.dto.ConcertCreateUpdate;
import com.itacademy.dto.user.User;
import com.itacademy.service.converters.ConcertToConcertReadConverter;
import com.itacademy.service.converters.PageToCustomPageConverter;
import com.itacademy.service.custom_exception.multiple.ErrorsDefinition;
import java.time.OffsetDateTime;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Validated
@Transactional(readOnly = true)
public class ConcertService implements IConcertService {

  private final IConcertDao concertDao;
  private final UserHolder holder;
  private final ConcertCreateToConcertConverter concertCreateToConcertConverter;
  private final ConcertToConcertReadConverter concertToConcertReadConverter;
  private final ConcertUpdateToConcertConverter concertUpdateToConcertConverter;
  private final PageToCustomPageConverter pageToCustomPageConverter;

  public ConcertService(IConcertDao concertDao, UserHolder holder,
      ConcertCreateToConcertConverter concertCreateToConcertConverter,
      ConcertToConcertReadConverter concertToConcertReadConverter,
      ConcertUpdateToConcertConverter concertUpdateToConcertConverter,
      PageToCustomPageConverter pageToCustomPageConverter) {
    this.concertDao = concertDao;
    this.holder = holder;
    this.concertCreateToConcertConverter = concertCreateToConcertConverter;
    this.concertToConcertReadConverter = concertToConcertReadConverter;
    this.concertUpdateToConcertConverter = concertUpdateToConcertConverter;
    this.pageToCustomPageConverter = pageToCustomPageConverter;
  }

  @Override
  public void check(ConcertCreateUpdate eventCreateUpdate) throws Multiple400Exception {

    final String dtEventFieldError = "Field must be later than now";
    final String dtEndOfSaleFieldError = "Field must be later than now and less than dt_event";
    final String categoryFieldError = "There are no such country in classifier";

    List<EachErrorDefinition> eachErrorDefinitions = new ArrayList<>();

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
      String url = "http://ClassifierService:80/api/v1/classifier/concert/category/" + eventCreateUpdate.getCategoryUuid();
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
  @Transactional
  public void add(@Valid ConcertCreateUpdate concertCreate) throws Multiple400Exception {
    check(concertCreate);
    concertDao.save(concertCreateToConcertConverter.convert(concertCreate));
  }

  @Override
  @Transactional
  public void update(@Valid ConcertCreateUpdate concertUpdate, UUID uuid, Long dtUpdate) throws Multiple400Exception {

    check(concertUpdate);

    Concert concert = concertDao.findByUuid(uuid);

    if(concert == null) {
      throw new IllegalArgumentException("Concert with such uuid is not found");
    }

    Long update = concert.getDtUpdate().toInstant().toEpochMilli();

    if(!update.equals(dtUpdate) ) {
      throw new IllegalArgumentException("Update time is not valid");
    }

    if(concert.getAuthorUuid().equals(holder.getUser().getUuid()) || holder.hasRoleAdmin()) {
      concertDao.save(concertUpdateToConcertConverter.convert(concertUpdate, concert));
    } else {
      throw new IllegalArgumentException("User is not the author or is not Admin");
    }
  }

  @Override
  public CustomPage<Concert> getCustomPage(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("title"));

    if (holder.isAuthenticated()) {
      if (holder.hasRoleAdmin()) {
        Page page1 = concertDao.findAll(pageable);

        if (page + 1 > page1.getTotalPages()) {
          throw new IllegalArgumentException("There are less pages than you want");
        }
        return pageToCustomPageConverter.convert(page1);
      } else {
        User user = holder.getUser();

        Page page1 = concertDao.findByEventStatusOrAuthorUuid(EventStatus.PUBLISHED, user.getUuid(), pageable);

        if (page + 1 > page1.getTotalPages()) {
          throw new IllegalArgumentException("There are less pages than you want");
        }
        return pageToCustomPageConverter.convert(page1);
      }
    } else {

      Page page1 = concertDao.findByEventStatus(EventStatus.PUBLISHED, pageable);

      if (page + 1 > page1.getTotalPages()) {
        throw new IllegalArgumentException("There are less pages than you want");
      }
      return pageToCustomPageConverter.convert(page1);
    }
  }

  @Override
  public ConcertRead getConcertByUuid(UUID uuid) {
    Concert concert = concertDao.findByUuid(uuid);
    if(concert == null) {
      throw new IllegalArgumentException("User with such uuid is not found");
    }
    return concertToConcertReadConverter.convert(concert);
  }
}
