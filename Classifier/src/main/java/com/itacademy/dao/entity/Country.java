package com.itacademy.dao.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table( name = "country", schema = "countries_and_categories")
public class Country implements Serializable {

  private static final long serialVersionUID = 2L;

  @Id
  private UUID uuid;

  @Column(name = "dt_create")
  private OffsetDateTime dtCreate;

  @Version
  @Column(name = "dt_update")
  private OffsetDateTime dtUpdate;

  private String title;

  private String description;

  public Country() {
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
