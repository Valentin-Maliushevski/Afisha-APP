package com.itacademy.dto;

import com.itacademy.dao.entity.UserStatus;
import java.util.Set;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserRegistrationByAdmin {

  @NotBlank
  private String mail;

  @NotBlank
  private String nick;

  @Transient
  private Set<RoleName> role;

  @NotNull
  private UserStatus userStatus;

  @NotBlank
  private String password;

  public UserRegistrationByAdmin() {
  }

  public String getMail() {
    return mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public String getNick() {
    return nick;
  }

  public void setNick(String nick) {
    this.nick = nick;
  }

  public Set<RoleName> getRole() {
    return role;
  }

  public void setRole(Set<RoleName> role) {
    this.role = role;
  }

  public UserStatus getUserStatus() {
    return userStatus;
  }

  public void setUserStatus(UserStatus userStatus) {
    this.userStatus = userStatus;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
