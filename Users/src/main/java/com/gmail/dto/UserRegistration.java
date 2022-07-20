package com.gmail.dto;

import javax.validation.constraints.NotBlank;

public class UserRegistration {

  @NotBlank
  private String mail;

  @NotBlank
  private String nick;

  @NotBlank
  private String password;

  public String getMail() {
    return mail;
  }

  public String getNick() {
    return nick;
  }

  public String getPassword() {
    return password;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public void setNick(String nick) {
    this.nick = nick;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
