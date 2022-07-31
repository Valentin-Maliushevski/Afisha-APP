package com.itacademy.service.api;

import com.itacademy.dto.CustomPage;
import com.itacademy.dao.entity.Country;
import com.itacademy.dto.CountryCreate;
import java.util.UUID;
import javax.validation.Valid;

public interface ICountryService {

  void addCountry(@Valid CountryCreate countryCreate);

  CustomPage<Country> getCustomPage(int page, int size);

  Country getCountryByUuid(UUID uuid);

}
