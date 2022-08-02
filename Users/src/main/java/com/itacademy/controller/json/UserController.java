package com.itacademy.controller.json;

import com.itacademy.controller.utils.JwtTokenUtil;
import com.itacademy.dto.UserLogin;
import com.itacademy.dto.UserRegistration;
import com.itacademy.dto.UserWithoutPassword;
import com.itacademy.service.UserHolder;
import com.itacademy.service.api.IUserService;
import com.itacademy.service.converters.UserToUserWithoutPasswordConverter;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  IUserService userService;
  @Autowired
  PasswordEncoder encoder;
  @Autowired
  UserHolder holder;
  @Autowired
  UserToUserWithoutPasswordConverter userToUserWithoutPasswordConverter;

  @RequestMapping(value = "/registration", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  public void create(@RequestBody UserRegistration dto) {
     userService.add(dto);
  }

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public String login(@RequestBody UserLogin loginDto) {
    UserDetails user = userService.loadUserByUsername(loginDto.getMail());

    if(!encoder.matches(loginDto.getPassword(), user.getPassword())) {
      throw new IllegalArgumentException("Password is wrong");
    }

    return JwtTokenUtil.generateAccessToken(user);
  }

  @RequestMapping(value = "/me")
  public ResponseEntity<UserWithoutPassword> getByToken() {
    return new ResponseEntity<>( userToUserWithoutPasswordConverter.convert(holder.getUser()), HttpStatus.OK);
  }
}
