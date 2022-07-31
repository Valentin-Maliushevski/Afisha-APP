package com.itacademy.controller.json;

import com.itacademy.dto.CustomPage;
import com.itacademy.dao.entity.Country;
import com.itacademy.dto.CountryCreate;
import com.itacademy.service.api.ICountryService;
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
  public void create(@RequestBody CountryCreate countryCreate) {
    this.countryService.addCountry(countryCreate);
  }

  @GetMapping
  public ResponseEntity<CustomPage<Country>> findPaginated(@RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "20") int size) {
    return new ResponseEntity<>(this.countryService.getCustomPage(page, size),HttpStatus.OK);
  }

  @GetMapping("/{uuid}")
  public ResponseEntity<Country>  get(@PathVariable UUID uuid) {
    return new ResponseEntity<>(this.countryService.getCountryByUuid(uuid), HttpStatus.OK);
  }

}
