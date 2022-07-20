package com.gmail.dto;

import javax.validation.constraints.NotBlank;

public class UserLogin {

  @NotBlank
  private String mail;

  @NotBlank
  private String password;

  public String getMail() {
    return mail;
  }

  public String getPassword() {
    return password;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
