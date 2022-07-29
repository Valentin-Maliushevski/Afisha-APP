package com.gmail.service.api;

import com.gmail.dao.entity.User;
import com.gmail.dto.CustomPage;
import com.gmail.dto.UserRegistrationByAdmin;
import com.gmail.dto.UserWithoutPassword;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IAdminService extends UserDetailsService {

  void add(@Valid UserRegistrationByAdmin userCreationByAdmin)
      throws Multiple400Exception, SingleException;

  void update(@Valid UserRegistrationByAdmin dto, UUID uuid, Long lastKnowUpdate)
      throws Multiple400Exception, SingleException;

  CustomPage<UserWithoutPassword> getCustomPage(int page, int size) throws SingleException;

  UserWithoutPassword getUserByUuid(UUID uuid) throws SingleException;

}
