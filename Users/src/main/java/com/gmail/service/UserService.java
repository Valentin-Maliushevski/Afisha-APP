package com.gmail.service;

import com.gmail.dao.api.IUserRepository;
import com.gmail.dao.api.IRoleRepository;
import com.gmail.dao.entity.User;
import com.gmail.dto.UserRegistration;
import com.gmail.service.api.IUserService;
import com.gmail.service.converters.UserRegistrationToUserConverter;
import com.gmail.service.custom_exception.multiple.EachErrorDefinition;
import com.gmail.service.custom_exception.multiple.ErrorsDefinition;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.util.ArrayList;
import java.util.List;
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
  public void check(UserRegistration userRegistration) throws Multiple400Exception, SingleException {

    User userFromDB1 = repository.findByUsername(userRegistration.getMail());
    User userFromDB2 = repository.findByNick(userRegistration.getNick());

    if(userFromDB1 == null && userFromDB2 == null) return;

    final String emailFieldError = "User with such e-mail is already exist";
    final String nickFieldError = "User with such nick is already exist";

    List<EachErrorDefinition> eachErrorDefinitions = new ArrayList<>();

    if(userFromDB1 != null) {

        EachErrorDefinition errorDefinition1 = new EachErrorDefinition("email", emailFieldError);
        eachErrorDefinitions.add(errorDefinition1);

      if(userFromDB1.getNick().equals(userRegistration.getNick())) {
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
  @Transactional
  public void add(UserRegistration userRegistration) throws Multiple400Exception, SingleException {
    check(userRegistration);
    this.repository.save(userRegistrationToUserConverter.convert(userRegistration));
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    UserDetails user = repository.findByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException("пользователь не найден");
    }
    return user;
  }

}
