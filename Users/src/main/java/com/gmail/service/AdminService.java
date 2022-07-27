package com.gmail.service;

import com.gmail.dao.api.IUserRepository;
import com.gmail.dao.api.IRoleRepository;
import com.gmail.dao.entity.Role;
import com.gmail.dao.entity.User;
import com.gmail.dto.CustomPage;
import com.gmail.dto.RoleName;
import com.gmail.dto.UserRegistrationByAdmin;
import com.gmail.dto.UserWithoutPassword;
import com.gmail.service.api.IAdminService;
import com.gmail.service.converters.UserToUserWithoutPasswordConverter;
import com.gmail.service.custom_exception.multiple.EachErrorDefinition;
import com.gmail.service.custom_exception.multiple.ErrorsDefinition;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
  private final PasswordEncoder encoder;
  private final UserToUserWithoutPasswordConverter userToUserWithoutPasswordConverter;

  public AdminService(IUserRepository repository, IRoleRepository roleRepository,
      PasswordEncoder encoder,
      UserToUserWithoutPasswordConverter userToUserWithoutPasswordConverter) {
    this.repository = repository;
    this.roleRepository = roleRepository;
    this.encoder = encoder;
    this.userToUserWithoutPasswordConverter = userToUserWithoutPasswordConverter;
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

   Set<Role> roles = new HashSet<>();

   for (RoleName roleName : userRegistrationByAdmin.getRole()) {
     if(roleName.getName().equals("ADMIN")) {
       roles.add(new Role(2L, "ADMIN"));
     }
     if(roleName.getName().equals("USER")) {
       roles.add(new Role(1L, "USER"));
     }
   }

   User user = new User();
   user.setUuid(UUID.randomUUID());
   user.setDtCreate(OffsetDateTime.now());
   user.setDtUpdate(user.getDtCreate());
   user.setUsername(userRegistrationByAdmin.getMail());
   user.setNick(userRegistrationByAdmin.getNick());
   user.setRoles(roles);
   user.setUserStatus(userRegistrationByAdmin.getUserStatus());
   user.setPassword(encoder.encode(userRegistrationByAdmin.getPassword()));
   user.setCredentialsNonExpired(true);
   user.setAccountNonExpired(true);
   user.setAccountNonLocked(true);
   user.setEnabled(true);

   this.repository.save(user);
  }

  @Override
  @Transactional
  public void update(@Valid UserRegistrationByAdmin userRegistrationByAdmin, UUID uuid, Long dtUpdate)
      throws Multiple400Exception, SingleException {

    check(userRegistrationByAdmin);

    User user = this.repository.findByUuid(uuid);

    if(user == null) {
      throw new SingleException();
    }

    Long update = user.getDtUpdate().toInstant().toEpochMilli();

    if(!update.equals(dtUpdate)) {
      throw new SingleException();
    }

    Set<Role> roles = new HashSet<>();

    for (RoleName roleName : userRegistrationByAdmin.getRole()) {
      if(roleName.getName().equalsIgnoreCase("ADMIN")) {
        roles.add(new Role(2L, "ADMIN"));
      }
      if(roleName.getName().equalsIgnoreCase("USER")) {
        roles.add(new Role(1L, "USER"));
      }
    }
    user.setUsername(userRegistrationByAdmin.getMail());
    user.setNick(userRegistrationByAdmin.getNick());
    user.setRoles(roles);
    user.setUserStatus(userRegistrationByAdmin.getUserStatus());
    user.setPassword(encoder.encode(userRegistrationByAdmin.getPassword()));

    this.repository.save(user);
  }

  @Override
  public CustomPage<UserWithoutPassword> getCustomPage(int page, int size) throws SingleException {
    Pageable pageable = PageRequest.of(page, size, Sort.by("nick"));

    Page page1 = this.repository.findAll(pageable);

    if (page + 1 > page1.getTotalPages()) {
      throw new SingleException();
    }

    List<UserWithoutPassword> users = new ArrayList<>();

    for (int i = 0; i < page1.getContent().size(); i++) {
      User user = (User) page1.getContent().get(i);
      UserWithoutPassword userWithoutPassword = userToUserWithoutPasswordConverter.convert(user);
      users.add(userWithoutPassword);
    }

    CustomPage<UserWithoutPassword> usersPage = new CustomPage<>();
    usersPage.setNumber(page);
    usersPage.setSize(size);
    usersPage.setTotalPages(page1.getTotalPages());
    usersPage.setTotalElements(page1.getTotalElements());
    usersPage.setNumberOfElements(page1.getContent().size());
    usersPage.setFirstPage(page1.isFirst());
    usersPage.setLastPage(page1.isLast());
    usersPage.setContent(users);

    return usersPage;
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
