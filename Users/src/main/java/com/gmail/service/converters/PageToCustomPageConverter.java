package com.gmail.service.converters;

import com.gmail.dao.entity.User;
import com.gmail.dto.CustomPage;
import com.gmail.dto.UserWithoutPassword;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class PageToCustomPageConverter implements Converter<Page<User>, CustomPage<UserWithoutPassword>> {

  private final UserToUserWithoutPasswordConverter userToUserWithoutPasswordConverter;

  public PageToCustomPageConverter(
      UserToUserWithoutPasswordConverter userToUserWithoutPasswordConverter) {
    this.userToUserWithoutPasswordConverter = userToUserWithoutPasswordConverter;
  }

  @Override
  public CustomPage<UserWithoutPassword> convert(Page<User> page) {
    List<UserWithoutPassword> users = new ArrayList<>();

    for (int i = 0; i < page.getContent().size(); i++) {
      User user = (User) page.getContent().get(i);
      UserWithoutPassword userWithoutPassword = userToUserWithoutPasswordConverter.convert(user);
      users.add(userWithoutPassword);
    }

    CustomPage<UserWithoutPassword> usersPage = new CustomPage<>();
    usersPage.setNumber(page.getNumber());
    usersPage.setSize(page.getSize());
    usersPage.setTotalPages(page.getTotalPages());
    usersPage.setTotalElements(page.getTotalElements());
    usersPage.setNumberOfElements(page.getContent().size());
    usersPage.setFirstPage(page.isFirst());
    usersPage.setLastPage(page.isLast());
    usersPage.setContent(users);

    return usersPage;
  }
}
