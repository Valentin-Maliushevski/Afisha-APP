package com.itacademy.service;

import com.itacademy.dao.api.IRoleRepository;
import com.itacademy.dao.api.IUserRepository;
import com.itacademy.dto.UserRegistration;
import com.itacademy.service.api.IUserService;
import com.itacademy.service.converters.UserRegistrationToUserConverter;
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
  public void add(UserRegistration userRegistration) {
    this.repository.save(userRegistrationToUserConverter.convert(userRegistration));
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDetails user = repository.findByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException("User with such mail is not found");
    }
    return user;
  }
}
