package com.gmail.service.api;

import com.gmail.dao.entity.User;
import com.gmail.dto.UserRegistration;;
import com.gmail.dto.UserWithoutPassword;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import javax.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {

  void check(UserRegistration userRegistration) throws Multiple400Exception, SingleException;

  void add(@Valid UserRegistration userRegistration) throws Multiple400Exception, SingleException;

}
