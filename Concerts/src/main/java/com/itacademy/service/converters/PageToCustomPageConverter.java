package com.itacademy.service.converters;

import com.itacademy.dao.entity.Concert;
import com.itacademy.dto.CustomPage;
import com.itacademy.dto.ConcertRead;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageToCustomPageConverter implements Converter<Page<Concert>, CustomPage<ConcertRead>> {

  private final ConcertToConcertReadConverter concertToConcertReadConverter;

  public PageToCustomPageConverter(ConcertToConcertReadConverter concertToConcertReadConverter) {
    this.concertToConcertReadConverter = concertToConcertReadConverter;
  }

  @Override
  public CustomPage<ConcertRead> convert(Page<Concert> page) {
    CustomPage<ConcertRead> newPage = new CustomPage<>();
    List<ConcertRead> concertsContent = new ArrayList<>();

    for(Concert concert : page.getContent()) {
      concertsContent.add(concertToConcertReadConverter.convert(concert));
    }

    newPage.setNumber(page.getNumber());
    newPage.setSize(page.getSize());
    newPage.setTotalPages(page.getTotalPages());
    newPage.setTotalElements(page.getTotalElements());
    newPage.setNumberOfElements(page.getNumberOfElements());
    newPage.setFirstPage(page.isFirst());
    newPage.setLastPage(page.isLast());
    newPage.setContent(concertsContent);

    return newPage;
  }
}
