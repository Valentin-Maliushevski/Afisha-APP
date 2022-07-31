package com.itacademy.service.api;

import com.itacademy.dto.UserRegistration;
import javax.validation.Valid;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {

  void add(@Valid UserRegistration userRegistration);

}
