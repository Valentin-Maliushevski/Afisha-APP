package com.gmail.controller.json;

import com.gmail.dao.entity.Concert;
import com.gmail.dto.CustomPage;
import com.gmail.dto.ConcertCreateUpdate;
import com.gmail.service.api.IConcertService;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/afisha/event/concerts")
public class ConcertController {

  private final IConcertService concertService;

  public ConcertController(IConcertService eventService) {
    this.concertService = eventService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void create(@RequestBody ConcertCreateUpdate dto)throws Multiple400Exception, SingleException {
    this.concertService.add(dto);
  }

  @GetMapping("/{uuid}")
  public ResponseEntity<Concert> get(@PathVariable UUID uuid) throws Multiple400Exception {
    return new ResponseEntity<>(this.concertService.getConcertByUuid(uuid), HttpStatus.OK);
  }

//    @GetMapping("/title/{title}")
//  public ResponseEntity<Concert> getByTitle(@PathVariable String title) throws SingleException {
//    Concert concert = this.concertService.getConcertByTitle(title);
//    if(concert == null) {
//      throw new SingleException();
//    }
//    return new ResponseEntity<>(concert, HttpStatus.OK);
//  }

  @GetMapping
  public ResponseEntity<CustomPage<Concert>> findPaginated(@RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "20") int size) throws SingleException {
    return new ResponseEntity<>(this.concertService.getCustomPage(page, size), HttpStatus.OK);
  }

  @PutMapping("/{uuid}/dt_update/{dt_update}")
  @ResponseStatus(HttpStatus.OK)
  public void update(@RequestBody ConcertCreateUpdate dto,
      @PathVariable UUID uuid,  @PathVariable Long dt_update)
      throws Multiple400Exception, SingleException {
    this.concertService.update(dto, uuid, dt_update);
  }
}
