package com.gmail.service;

import com.gmail.dto.CustomPage;
import com.gmail.dao.api.ICountryDao;
import com.gmail.dao.entity.Country;
import com.gmail.dto.CountryCreate;
import com.gmail.service.api.ICountryService;
import com.gmail.service.custom_exception.multiple.EachErrorDefinition;
import com.gmail.service.custom_exception.multiple.ErrorsDefinition;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CountryService implements ICountryService {

  private final ICountryDao countryDao;

  public CountryService(ICountryDao countryDao) {
    this.countryDao = countryDao;
  }

  @Override
  public void check(CountryCreate countryCreate) throws Multiple400Exception, SingleException {

    List<EachErrorDefinition> eachErrorDefinitions = new ArrayList<>();

    final String titleFieldError = "Country with such title is already exist";

    if(getCountryByTitle(countryCreate.getTitle()) != null) {
      EachErrorDefinition errorDefinition = new EachErrorDefinition("title", titleFieldError);
      eachErrorDefinitions.add(errorDefinition);
    }

    if(!eachErrorDefinitions.isEmpty()) {
      ErrorsDefinition errorsDefinition = new ErrorsDefinition(eachErrorDefinitions);
      throw new Multiple400Exception(errorsDefinition);
    }
  }

  @Override
  public void addCountry(CountryCreate countryCreate) throws Multiple400Exception, SingleException {

    check(countryCreate);

    Country country = new Country();
    country.setUuid(UUID.randomUUID());
    country.setDescription(countryCreate.getDescription());
    country.setTitle(countryCreate.getTitle());
    country.setDtCreate(OffsetDateTime.now());
    country.setDtUpdate(country.getDtCreate());

    this.countryDao.save(country);
  }

  @Override
  public CustomPage<Country> getCustomPage(int page, int size) throws SingleException {

    Pageable pageable = PageRequest.of(page, size, Sort.by("title"));

    Page page1 = this.countryDao.findAll(pageable);

    if (page + 1 > page1.getTotalPages()) {
      throw new SingleException();
    }

    CustomPage<Country> countriesPage = new CustomPage<>();
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
  public Country getCountryByUuid(UUID uuid) throws SingleException {
    Country country = this.countryDao.findByUuid(uuid);
    if(country == null) {
      throw new SingleException();
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
