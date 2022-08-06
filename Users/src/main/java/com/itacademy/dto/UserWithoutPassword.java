package com.itacademy.dto;

import com.itacademy.dao.entity.UserStatus;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

public class UserWithoutPassword {

  private UUID uuid;

  private OffsetDateTime dtCreate;

  private OffsetDateTime dtUpdate;

  private String mail;

  private String nick;

  private Set<RoleName> role;

  private UserStatus userStatus;

  public UserWithoutPassword() {
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public OffsetDateTime getDtCreate() {
    return dtCreate;
  }

  public void setDtCreate(OffsetDateTime dtCreate) {
    this.dtCreate = dtCreate;
  }

  public OffsetDateTime getDtUpdate() {
    return dtUpdate;
  }

  public void setDtUpdate(OffsetDateTime dtUpdate) {
    this.dtUpdate = dtUpdate;
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
}
