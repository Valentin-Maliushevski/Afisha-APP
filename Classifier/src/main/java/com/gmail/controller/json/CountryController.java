package com.gmail.controller.json;

import com.gmail.dto.CustomPage;
import com.gmail.dao.entity.Country;
import com.gmail.dto.CountryCreate;
import com.gmail.service.api.ICountryService;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/classifier/country")
public class CountryController {

  private final ICountryService countryService;

  public CountryController(ICountryService countryService) {
    this.countryService = countryService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void create(@RequestBody CountryCreate countryCreate)
      throws Multiple400Exception, SingleException {
    this.countryService.addCountry(countryCreate);
  }

  @GetMapping
  public ResponseEntity<CustomPage<Country>> findPaginated(@RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "20") int size) throws SingleException {
    return new ResponseEntity<>(this.countryService.getCustomPage(page, size),HttpStatus.OK);
  }

  @GetMapping("/{uuid}")
  public ResponseEntity<Country>  get(@PathVariable UUID uuid) throws  Multiple400Exception {
    return new ResponseEntity<>(this.countryService.getCountryByUuid(uuid), HttpStatus.OK);
  }

}
