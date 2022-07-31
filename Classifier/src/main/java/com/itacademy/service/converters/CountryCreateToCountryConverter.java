package com.itacademy.service.converters;

import com.itacademy.dao.entity.Country;
import com.itacademy.dto.CountryCreate;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class CountryCreateToCountryConverter implements Converter<CountryCreate, Country> {

  @Override
  public Country convert(CountryCreate countryCreate) {
    Country country = new Country();
    country.setUuid(UUID.randomUUID());
    country.setDescription(countryCreate.getDescription());
    country.setTitle(countryCreate.getTitle());
    country.setDtCreate(OffsetDateTime.now());
    country.setDtUpdate(country.getDtCreate());

    return country;
  }
}
