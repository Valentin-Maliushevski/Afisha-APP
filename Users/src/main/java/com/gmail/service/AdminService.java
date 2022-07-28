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
import com.gmail.service.custom_exception.multiple.EachErrorDefinition;
import com.gmail.service.custom_exception.multiple.ErrorsDefinition;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
  public void check(UserRegistrationByAdmin userCreationByAdmin) throws Multiple400Exception, SingleException {

    User userFromDB1 = repository.findByUsername(userCreationByAdmin.getMail());
    User userFromDB2 = repository.findByNick(userCreationByAdmin.getNick());

    if(userFromDB1 == null && userFromDB2 == null) return;

    final String emailFieldError = "User with such e-mail is already exist";
    final String nickFieldError = "User with such nick is already exist";

    List<EachErrorDefinition> eachErrorDefinitions = new ArrayList<>();

    if(userFromDB1 != null) {

      EachErrorDefinition errorDefinition1 = new EachErrorDefinition("email", emailFieldError);
      eachErrorDefinitions.add(errorDefinition1);

      if(userFromDB1.getNick().equals(userCreationByAdmin.getNick())) {
        EachErrorDefinition errorDefinition2 = new EachErrorDefinition("nick", nickFieldError);
        eachErrorDefinitions.add(errorDefinition2);
      }
      ErrorsDefinition errorsDefinition = new ErrorsDefinition(eachErrorDefinitions);
      throw new Multiple400Exception(errorsDefinition);
    }

    EachErrorDefinition errorDefinition = new EachErrorDefinition("nick", nickFieldError);
    eachErrorDefinitions.add(errorDefinition);
    ErrorsDefinition errorsDefinition = new ErrorsDefinition(eachErrorDefinitions);
    throw new Multiple400Exception(errorsDefinition);
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
    check(userRegistrationByAdmin);

   this.repository.save(userRegistrationByAdminToUserConverter.convert(userRegistrationByAdmin));
  }

  @Override
  @Transactional
  public void update(@Valid UserRegistrationByAdmin userRegistrationByAdmin, UUID uuid, Long dtUpdate)
      throws Multiple400Exception, SingleException {

    check(userRegistrationByAdmin);

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
