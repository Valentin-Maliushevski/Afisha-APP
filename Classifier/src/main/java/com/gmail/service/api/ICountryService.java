package com.gmail.service.api;

import com.gmail.dto.CustomPage;
import com.gmail.dao.entity.Country;
import com.gmail.dto.CountryCreate;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.util.UUID;
import javax.validation.Valid;

public interface ICountryService {

  void addCountry(@Valid CountryCreate countryCreate) throws Multiple400Exception, SingleException;

  CustomPage<Country> getCustomPage(int page, int size) throws SingleException;

  Country getCountryByUuid(UUID uuid) throws  Multiple400Exception;

  Country getCountryByTitle(String title) throws SingleException;

}
