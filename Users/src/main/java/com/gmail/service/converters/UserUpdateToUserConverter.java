package com.gmail.service.converters;

import com.gmail.dao.entity.Role;
import com.gmail.dao.entity.User;
import com.gmail.dto.RoleName;
import com.gmail.dto.UserRegistrationByAdmin;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserUpdateToUserConverter {

  private final PasswordEncoder encoder;

  public UserUpdateToUserConverter(PasswordEncoder encoder) {
    this.encoder = encoder;
  }

  public User convert(UserRegistrationByAdmin userRegistrationByAdmin, User userFromDB) {
    Set<Role> roles = new HashSet<>();

    for (RoleName roleName : userRegistrationByAdmin.getRole()) {
      if(roleName.getName().equalsIgnoreCase("ADMIN")) {
        roles.add(new Role(2L, "ADMIN"));
      }
      if(roleName.getName().equalsIgnoreCase("USER")) {
        roles.add(new Role(1L, "USER"));
      }
    }

    userFromDB.setUsername(userRegistrationByAdmin.getMail());
    userFromDB.setNick(userRegistrationByAdmin.getNick());
    userFromDB.setRoles(roles);
    userFromDB.setUserStatus(userRegistrationByAdmin.getUserStatus());
    userFromDB.setPassword(encoder.encode(userRegistrationByAdmin.getPassword()));

    return userFromDB;
  }

}
