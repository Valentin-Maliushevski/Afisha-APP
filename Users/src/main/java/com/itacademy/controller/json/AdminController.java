package com.itacademy.controller.json;

import com.itacademy.dto.CustomPage;
import com.itacademy.dto.UserRegistrationByAdmin;
import com.itacademy.dto.UserWithoutPassword;
import com.itacademy.service.api.IAdminService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/v1/users")
public class AdminController {

  @Autowired
  IAdminService adminService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void create(@RequestBody UserRegistrationByAdmin dto) {
    adminService.add(dto);
  }

  @GetMapping("/{uuid}")
  public ResponseEntity<UserWithoutPassword> getByUuid(@PathVariable UUID uuid) {
    return new ResponseEntity<>( adminService.getUserByUuid(uuid), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<CustomPage<UserWithoutPassword>> findPaginated(@RequestParam(name = "page",
      defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "20") int size) {
    return new ResponseEntity<>(adminService.getCustomPage(page, size), HttpStatus.OK);
  }

  @PutMapping("/{uuid}/dt_update/{dt_update}")
  @ResponseStatus(HttpStatus.OK)
  public void update(@RequestBody UserRegistrationByAdmin dto,
      @PathVariable UUID uuid,  @PathVariable Long dt_update) {
    adminService.update(dto, uuid, dt_update);
  }

}
