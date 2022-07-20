package com.gmail.dao;

import com.gmail.dao.api.IUserRepository;
import com.gmail.dao.api.IRoleRepository;
import com.gmail.dao.entity.Role;
import com.gmail.dao.entity.User;
import com.gmail.dao.entity.UserStatus;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultUser implements CommandLineRunner {

  private final IRoleRepository roleRepository;
  private final IUserRepository repository;
  private final PasswordEncoder encoder;

  public DefaultUser(IRoleRepository roleRepository, IUserRepository repository, PasswordEncoder encoder) {
    this.roleRepository = roleRepository;
    this.repository = repository;
    this.encoder = encoder;
  }

  @Override
  public void run(String... args) throws Exception {
    User user = new User();
    user.setUuid(UUID.randomUUID());
    user.setDtCreate(OffsetDateTime.now());
    user.setDtUpdate(user.getDtCreate());
    user.setUsername("admin@gmail.com");
    user.setNick("admin");
    user.setRoles(Collections.singleton(new Role(2L,"ADMIN")));
    user.setUserStatus(UserStatus.ACTIVATED);
    user.setPassword(encoder.encode("123456"));
    user.setCredentialsNonExpired(true);
    user.setAccountNonExpired(true);
    user.setAccountNonLocked(true);
    user.setEnabled(true);

    this.repository.save(user);
  }
}
