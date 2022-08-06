package com.itacademy.service.converters;

import com.itacademy.dao.entity.User;
import com.itacademy.dto.RoleName;
import com.itacademy.dto.UserWithoutPassword;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserWithoutPasswordConverter implements Converter<User, UserWithoutPassword> {

  @Override
  public UserWithoutPassword convert(User user) {
    UserWithoutPassword userWithoutPassword = new UserWithoutPassword();
    userWithoutPassword.setUuid(user.getUuid());
    userWithoutPassword.setDtCreate(user.getDtCreate());
    userWithoutPassword.setDtUpdate(user.getDtUpdate());
    userWithoutPassword.setMail(user.getUsername());
    userWithoutPassword.setNick(user.getNick());
    userWithoutPassword.setRole(user.getRoles().stream()
        .map(p -> new RoleName(p.getAuthority())).collect(Collectors.toSet()));
    userWithoutPassword.setUserStatus(user.getUserStatus());

    return userWithoutPassword;
  }
}
