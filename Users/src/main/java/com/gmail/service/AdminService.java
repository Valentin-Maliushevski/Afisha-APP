package com.gmail.service;

import com.gmail.dao.api.IUserRepository;
import com.gmail.dao.api.IRoleRepository;
import com.gmail.dao.entity.User;
import com.gmail.dto.CustomPage;
import com.gmail.dto.UserRegistrationByAdmin;
import com.gmail.dto.UserWithoutPassword;
import com.gmail.service.api.IAdminService;
import com.gmail.service.converters.PageToCustomPageConverter;
import com.gmail.service.converters.UserRegistrationByAdminToUserConverter;
import com.gmail.service.converters.UserToUserWithoutPasswordConverter;
import com.gmail.service.converters.UserUpdateToUserConverter;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.util.UUID;
import javax.validation.Valid;
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

  private final IUserRepository repository;
  private final IRoleRepository roleRepository;
  private final UserToUserWithoutPasswordConverter userToUserWithoutPasswordConverter;
  private final UserRegistrationByAdminToUserConverter userRegistrationByAdminToUserConverter;
  private final UserUpdateToUserConverter userUpdateToUserConverter;
  private final PageToCustomPageConverter pageToCustomPageConverter;

  public AdminService(IUserRepository repository, IRoleRepository roleRepository,
      UserToUserWithoutPasswordConverter userToUserWithoutPasswordConverter,
      UserRegistrationByAdminToUserConverter userRegistrationByAdminToUserConverter,
      UserUpdateToUserConverter userUpdateToUserConverter,
      PageToCustomPageConverter pageToCustomPageConverter) {
    this.repository = repository;
    this.roleRepository = roleRepository;
    this.userToUserWithoutPasswordConverter = userToUserWithoutPasswordConverter;
    this.userRegistrationByAdminToUserConverter = userRegistrationByAdminToUserConverter;
    this.userUpdateToUserConverter = userUpdateToUserConverter;
    this.pageToCustomPageConverter = pageToCustomPageConverter;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = repository.findByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }
    return user;
  }

  @Override
  @Transactional
  public void add(@Valid UserRegistrationByAdmin userRegistrationByAdmin)
      throws Multiple400Exception, SingleException {

   this.repository.save(userRegistrationByAdminToUserConverter.convert(userRegistrationByAdmin));
  }

  @Override
  @Transactional
  public void update(@Valid UserRegistrationByAdmin userRegistrationByAdmin, UUID uuid, Long dtUpdate)
      throws Multiple400Exception, SingleException {
    User userFromDB = this.repository.findByUuid(uuid);

    if(userFromDB == null) {
      throw new SingleException();
    }

    Long update = userFromDB.getDtUpdate().toInstant().toEpochMilli();

    if(!update.equals(dtUpdate)) {
      throw new SingleException();
    }

    this.repository.save(userUpdateToUserConverter.convert(userRegistrationByAdmin, userFromDB));
  }

  @Override
  public CustomPage<UserWithoutPassword> getCustomPage(int page, int size) throws SingleException {
    Pageable pageable = PageRequest.of(page, size, Sort.by("nick"));

    Page page1 = this.repository.findAll(pageable);

    if (page + 1 > page1.getTotalPages()) {
      throw new SingleException();
    }

    return pageToCustomPageConverter.convert(page1);
  }

  @Override
  public UserWithoutPassword getUserByUuid(UUID uuid) throws SingleException {
    User user = this.repository.findByUuid(uuid);
    if(user == null) {
      throw new SingleException();
    }
    return userToUserWithoutPasswordConverter.convert(user);
  }

}
