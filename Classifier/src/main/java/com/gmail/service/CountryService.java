package com.gmail.service;

import com.gmail.dto.CustomPage;
import com.gmail.dao.api.ICountryDao;
import com.gmail.dao.entity.Country;
import com.gmail.dto.CountryCreate;
import com.gmail.service.api.ICountryService;
import com.gmail.service.converters.CountryCreateToCountryConverter;
import com.gmail.service.converters.CountryPageToCustomPageConverter;
import com.gmail.service.custom_exception.multiple.EachErrorDefinition;
import com.gmail.service.custom_exception.multiple.ErrorsDefinition;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CountryService implements ICountryService {

  private final ICountryDao countryDao;
  private final CountryCreateToCountryConverter countryCreateToCountryConverter;
  private final CountryPageToCustomPageConverter countryPageToCustomPageConverter;

  public CountryService(ICountryDao countryDao,
      CountryCreateToCountryConverter countryCreateToCountryConverter,
      CountryPageToCustomPageConverter countryPageToCustomPageConverter) {
    this.countryDao = countryDao;
    this.countryCreateToCountryConverter = countryCreateToCountryConverter;
    this.countryPageToCustomPageConverter = countryPageToCustomPageConverter;
  }

  @Override
  @Transactional
  public void addCountry(CountryCreate countryCreate) throws Multiple400Exception, SingleException {
    this.countryDao.save(countryCreateToCountryConverter.convert(countryCreate));
  }

  @Override
  public CustomPage<Country> getCustomPage(int page, int size) throws SingleException {
    Pageable pageable = PageRequest.of(page, size, Sort.by("title"));

    Page page1 = this.countryDao.findAll(pageable);

    if (page + 1 > page1.getTotalPages()) {
      throw new SingleException();
    }

    return countryPageToCustomPageConverter.convert(page1);
  }

  @Override
  public Country getCountryByUuid(UUID uuid)throws  Multiple400Exception{
    Country country = this.countryDao.findByUuid(uuid);
    final String uuidError = "There are no country with such uuid";
    List<EachErrorDefinition> eachErrorDefinitions = new ArrayList<>();
    if(country == null) {
      EachErrorDefinition errorDefinition = new EachErrorDefinition("uuid", uuidError);
     eachErrorDefinitions.add(errorDefinition);
    }
    if(!eachErrorDefinitions.isEmpty()) {
      ErrorsDefinition errorsDefinition = new ErrorsDefinition(eachErrorDefinitions);
      throw new Multiple400Exception(errorsDefinition);
    }
    return country;
  }

  @Override
  public Country getCountryByTitle(String title) throws SingleException {
    if(title.isBlank()) {
      throw new SingleException();
    }
    return this.countryDao.findByTitleIgnoreCase(title);
  }

}
