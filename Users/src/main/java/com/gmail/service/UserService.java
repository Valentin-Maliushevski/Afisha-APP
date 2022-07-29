package com.gmail.service;

import com.gmail.dao.api.IUserRepository;
import com.gmail.dao.api.IRoleRepository;
import com.gmail.dto.UserRegistration;
import com.gmail.service.api.IUserService;
import com.gmail.service.converters.UserRegistrationToUserConverter;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class UserService implements IUserService {

  private final IUserRepository repository;
  private final IRoleRepository roleRepository;
  private final UserRegistrationToUserConverter userRegistrationToUserConverter;

  public UserService(IUserRepository repository, IRoleRepository roleRepository,
      UserRegistrationToUserConverter userRegistrationToUserConverter) {
    this.repository = repository;
    this.roleRepository = roleRepository;
    this.userRegistrationToUserConverter = userRegistrationToUserConverter;
  }

  @Override
  @Transactional
  public void add(UserRegistration userRegistration) throws Multiple400Exception, SingleException {
    this.repository.save(userRegistrationToUserConverter.convert(userRegistration));
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDetails user = repository.findByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }
    return user;
  }

}
