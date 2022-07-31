package com.itacademy.controller.json;

import com.itacademy.dto.CustomPage;
import com.itacademy.dto.FilmCreateUpdate;
import com.itacademy.dto.FilmRead;
import com.itacademy.service.custom_exception.multiple.Multiple400Exception;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.itacademy.service.api.IFilmService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/afisha/event/films")
public class FilmController {

  private final IFilmService filmService;

  public FilmController(IFilmService eventService) {
    this.filmService = eventService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void create(@RequestBody FilmCreateUpdate dto) throws Multiple400Exception {
    this.filmService.add(dto);
  }

  @GetMapping("/{uuid}")
  public ResponseEntity<FilmRead> getByUuid(@PathVariable UUID uuid) {
    return new ResponseEntity<>(filmService.getFilmByUuid(uuid), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<CustomPage<FilmRead>> findPaginated(@RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "20") int size) {
    return new ResponseEntity<>(filmService.getCustomPage(page, size), HttpStatus.OK);
  }

  @PutMapping("/{uuid}/dt_update/{dt_update}")
  @ResponseStatus(HttpStatus.OK)
  public void update(@RequestBody FilmCreateUpdate dto, @PathVariable UUID uuid,  @PathVariable Long dt_update)
      throws Multiple400Exception {
    filmService.update(dto, uuid, dt_update);
  }
}
