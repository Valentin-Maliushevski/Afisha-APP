package com.gmail.service;

import com.gmail.dao.entity.Concert;
import com.gmail.dao.entity.EventStatus;
import com.gmail.dto.ConcertRead;
import com.gmail.dto.CustomPage;
import com.gmail.dao.api.IConcertDao;
import com.gmail.dto.ConcertCreateUpdate;
import com.gmail.dto.user.User;
import com.gmail.service.api.IConcertService;
import com.gmail.service.converters.ConcertCreateToConcertConverter;
import com.gmail.service.converters.ConcertToConcertReadConverter;
import com.gmail.service.converters.ConcertUpdateToConcertConverter;
import com.gmail.service.converters.PageToCustomPageConverter;
import com.gmail.service.custom_exception.multiple.EachErrorDefinition;
import com.gmail.service.custom_exception.multiple.ErrorsDefinition;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
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
      String url = "http://localhost:8082/api/v1/classifier/concert/category/" + eventCreateUpdate.getCategoryUuid();
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
  public void add(@Valid ConcertCreateUpdate concertCreate)
      throws Multiple400Exception, SingleException {
    check(concertCreate);
    concertDao.save(concertCreateToConcertConverter.convert(concertCreate));
  }

  @Override
  @Transactional
  public void update(@Valid ConcertCreateUpdate concertUpdate, UUID uuid, Long dtUpdate)
      throws Multiple400Exception, SingleException {

    check(concertUpdate);

    if(uuid == null || uuid.toString().isEmpty()) {
      throw new SingleException();
    }

    Concert concert = concertDao.findByUuid(uuid);

    Long update = concert.getDtUpdate().toInstant().toEpochMilli();

    if(!update.equals(dtUpdate) ) {
      throw new SingleException();
    }

    if(concert.getAuthorUuid().equals(holder.getUser().getUuid()) || holder.hasRoleAdmin()) {
      concertDao.save(concertUpdateToConcertConverter.convert(concertUpdate, concert));
    } else {
      throw new SingleException();
    }
  }

  @Override
  public CustomPage<Concert> getCustomPage(int page, int size) throws SingleException {
    Pageable pageable = PageRequest.of(page, size, Sort.by("title"));

    if (holder.isAuthenticated()) {
      if (holder.hasRoleAdmin()) {
        Page page1 = concertDao.findAll(pageable);

        if (page + 1 > page1.getTotalPages()) {
          throw new SingleException();
        }
        return pageToCustomPageConverter.convert(page1);
      } else {
        User user = holder.getUser();

        Page page1 = concertDao.findByEventStatusOrAuthorUuid(EventStatus.PUBLISHED, user.getUuid(), pageable);

        if (page + 1 > page1.getTotalPages()) {
          throw new SingleException();
        }
        return pageToCustomPageConverter.convert(page1);
      }
    } else {

      Page page1 = concertDao.findByEventStatus(EventStatus.PUBLISHED, pageable);

      if (page + 1 > page1.getTotalPages()) {
        throw new SingleException();
      }
      return pageToCustomPageConverter.convert(page1);
    }
  }

  @Override
  public ConcertRead getConcertByUuid(UUID uuid) throws Multiple400Exception {
    Concert concert = concertDao.findByUuid(uuid);
    final String uuidError = "There are no concert with such uuid";
    List<EachErrorDefinition> eachErrorDefinitions = new ArrayList<>();

    if(concert == null) {
      EachErrorDefinition errorDefinition = new EachErrorDefinition("uuid", uuidError);
      eachErrorDefinitions.add(errorDefinition);
    }
    if(!eachErrorDefinitions.isEmpty()) {
      ErrorsDefinition errorsDefinition = new ErrorsDefinition(eachErrorDefinitions);
      throw new Multiple400Exception(errorsDefinition);
    }
    return concertToConcertReadConverter.convert(concert);
  }
}
