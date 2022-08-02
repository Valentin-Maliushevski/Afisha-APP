package com.itacademy.service.converters;

import com.itacademy.dao.entity.Country;
import com.itacademy.dto.CustomPage;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class CountryPageToCustomPageConverter implements Converter<Page<Country>, CustomPage<Country>> {

  @Override
  public CustomPage<Country> convert(Page<Country> page) {
    CustomPage<Country> countriesPage = new CustomPage<>();
    countriesPage.setNumber(page.getNumber());
    countriesPage.setSize(page.getSize());
    countriesPage.setTotalPages(page.getTotalPages());
    countriesPage.setTotalElements(page.getTotalElements());
    countriesPage.setNumberOfElements(page.getContent().size());
    countriesPage.setFirstPage(page.isFirst());
    countriesPage.setLastPage(page.isLast());
    countriesPage.setContent(page.getContent());

    return countriesPage;
  }
}
