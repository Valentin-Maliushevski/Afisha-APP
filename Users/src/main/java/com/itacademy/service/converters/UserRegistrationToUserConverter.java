package com.itacademy.service.converters;

import com.itacademy.dao.entity.Role;
import com.itacademy.dao.entity.User;
import com.itacademy.dao.entity.UserStatus;
import com.itacademy.dto.UserRegistration;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.UUID;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationToUserConverter implements Converter<UserRegistration, User> {

  private final PasswordEncoder encoder;

  public UserRegistrationToUserConverter(PasswordEncoder encoder) {
    this.encoder = encoder;
  }

  @Override
  public User convert(UserRegistration userRegistration) {
    User user = new User();
    user.setUuid(UUID.randomUUID());
    user.setDtCreate(OffsetDateTime.now());
    user.setDtUpdate(user.getDtCreate());
    user.setUsername(userRegistration.getMail());
    user.setNick(userRegistration.getNick());
    user.setRoles(Collections.singleton(new Role(1L, "USER")));
    user.setUserStatus(UserStatus.WAITING_ACTIVATION);
    user.setPassword(encoder.encode(userRegistration.getPassword()));
    user.setCredentialsNonExpired(true);
    user.setAccountNonExpired(true);
    user.setAccountNonLocked(true);
    user.setEnabled(true);

    return user;
  }
}
