package com.itacademy.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "t_role", schema = "users")
public class Role implements GrantedAuthority {

  @Id
  private Long id;

  private String name;

  public Role() {
  }

  public Role(String name) {
    this.name = name;
  }

  public Role(Long id) {
    this.id = id;
  }

  public Role(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getAuthority() {
    return getName();
  }
}
