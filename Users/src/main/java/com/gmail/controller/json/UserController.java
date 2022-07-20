package com.gmail.controller.json;

import com.gmail.controller.utils.JwtTokenUtil;
import com.gmail.dto.UserLogin;
import com.gmail.dto.UserRegistration;
import com.gmail.dto.UserWithoutPassword;
import com.gmail.service.UserHolder;
import com.gmail.service.api.IUserService;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final IUserService userService;
  private final PasswordEncoder encoder;
  private UserHolder holder;

  public UserController(IUserService userService, PasswordEncoder encoder, UserHolder holder) {
    this.userService = userService;
    this.encoder = encoder;
    this.holder = holder;
  }

  @RequestMapping(value = "/registration", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  public void create(@RequestBody UserRegistration dto) throws Multiple400Exception, SingleException {
     this.userService.add(dto);
  }

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public String login(@RequestBody UserLogin loginDto) throws SingleException {

    UserDetails user = this.userService.loadUserByUsername(loginDto.getMail());

    if(!encoder.matches(loginDto.getPassword(), user.getPassword())) {
      throw new SingleException();
    }

    return JwtTokenUtil.generateAccessToken(user);
  }

  @RequestMapping(value = "/me")
  public ResponseEntity<UserWithoutPassword> getByToken() throws SingleException {
    return new ResponseEntity<>( this.userService.mapUserToUserWithoutPassword(holder.getUser()), HttpStatus.OK);
  }
}
