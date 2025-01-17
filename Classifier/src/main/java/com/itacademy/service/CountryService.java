package com.itacademy.service;

import com.itacademy.dto.CustomPage;
import com.itacademy.service.api.ICountryService;
import com.itacademy.service.converters.CountryCreateToCountryConverter;
import com.itacademy.service.converters.CountryPageToCustomPageConverter;
import com.itacademy.dao.api.ICountryDao;
import com.itacademy.dao.entity.Country;
import com.itacademy.dto.CountryCreate;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CountryService implements ICountryService {

  @Autowired
  ICountryDao countryDao;
  @Autowired
  CountryCreateToCountryConverter countryCreateToCountryConverter;
  @Autowired
  CountryPageToCustomPageConverter countryPageToCustomPageConverter;

  @Override
  @Transactional
  public void addCountry(CountryCreate countryCreate) {
    countryDao.save(countryCreateToCountryConverter.convert(countryCreate));
  }

  @Override
  public CustomPage<Country> getCustomPage(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("title"));

    Page page1 = countryDao.findAll(pageable);

    if (page + 1 > page1.getTotalPages()) {
      throw new IllegalArgumentException("There are less pages than you want");
    }
    return countryPageToCustomPageConverter.convert(page1);
  }

  @Override
  public Country getCountryByUuid(UUID uuid) {
    Country country = countryDao.findByUuid(uuid);
    if(country == null) {
      throw new IllegalArgumentException("Country with such uuid is not found");
    }
    return country;
  }
}
