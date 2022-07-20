package com.gmail.controller.json;

import com.gmail.dto.CustomPage;
import com.gmail.dto.UserRegistrationByAdmin;
import com.gmail.dto.UserWithoutPassword;
import com.gmail.service.UserHolder;
import com.gmail.service.api.IAdminService;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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

  private final IAdminService adminService;
  private final PasswordEncoder encoder;
  private UserHolder holder;

  public AdminController(IAdminService adminService, PasswordEncoder encoder, UserHolder holder) {
    this.adminService = adminService;
    this.encoder = encoder;
    this.holder = holder;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void create(@RequestBody UserRegistrationByAdmin dto)throws Multiple400Exception, SingleException {
    this.adminService.add(dto);
  }

  @GetMapping("/{uuid}")
  public ResponseEntity<UserWithoutPassword> getByUuid(@PathVariable UUID uuid) throws SingleException {
    return new ResponseEntity<>( this.adminService.getUserByUuid(uuid), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<CustomPage<UserWithoutPassword>> findPaginated(@RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "20") int size) throws SingleException {
    return new ResponseEntity<>(this.adminService.getCustomPage(page, size), HttpStatus.OK);
  }

  @PutMapping("/{uuid}/dt_update/{dt_update}")
  @ResponseStatus(HttpStatus.OK)
  public void update(@RequestBody UserRegistrationByAdmin dto,
      @PathVariable UUID uuid,  @PathVariable Long dt_update)
      throws Multiple400Exception, SingleException {
    this.adminService.update(dto, uuid, dt_update);
  }

}
