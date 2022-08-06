package com.itacademy.service;

import com.itacademy.dao.api.IUserRepository;
import com.itacademy.dao.entity.User;
import com.itacademy.dto.CustomPage;
import com.itacademy.dto.UserRegistrationByAdmin;
import com.itacademy.dto.UserWithoutPassword;
import com.itacademy.service.api.IAdminService;
import com.itacademy.service.converters.PageToCustomPageConverter;
import com.itacademy.service.converters.UserRegistrationByAdminToUserConverter;
import com.itacademy.service.converters.UserToUserWithoutPasswordConverter;
import com.itacademy.service.converters.UserUpdateToUserConverter;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class AdminService implements IAdminService {

  @Autowired
  IUserRepository repository;
  @Autowired
  UserToUserWithoutPasswordConverter userToUserWithoutPasswordConverter;
  @Autowired
  UserRegistrationByAdminToUserConverter userRegistrationByAdminToUserConverter;
  @Autowired
  UserUpdateToUserConverter userUpdateToUserConverter;
  @Autowired
  PageToCustomPageConverter pageToCustomPageConverter;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = repository.findByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException("User with such mail is not found");
    }
    return user;
  }

  @Override
  @Transactional
  public void add(@Valid UserRegistrationByAdmin userRegistrationByAdmin) {
   repository.save(userRegistrationByAdminToUserConverter.convert(userRegistrationByAdmin));
  }

  @Override
  @Transactional
  public void update(@Valid UserRegistrationByAdmin userRegistrationByAdmin, UUID uuid, Long dtUpdate) {
    User userFromDB = this.repository.findByUuid(uuid);

    if(userFromDB == null) {
      throw new IllegalArgumentException("User with such uuid is not found");
    }

    Long update = userFromDB.getDtUpdate().toInstant().toEpochMilli();

    if(!update.equals(dtUpdate)) {
      throw new IllegalArgumentException("Update time is not valid");
    }

    repository.save(userUpdateToUserConverter.convert(userRegistrationByAdmin, userFromDB));
  }

  @Override
  public CustomPage<UserWithoutPassword> getCustomPage(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("nick"));

    Page page1 = repository.findAll(pageable);

    if (page + 1 > page1.getTotalPages()) {
      throw new IllegalArgumentException("There are less pages than you want");
    }

    return pageToCustomPageConverter.convert(page1);
  }

  @Override
  public UserWithoutPassword getUserByUuid(UUID uuid) {
    User user = repository.findByUuid(uuid);
    if(user == null) {
      throw new IllegalArgumentException("User with such uuid is not found");
    }
    return userToUserWithoutPasswordConverter.convert(user);
  }

}
