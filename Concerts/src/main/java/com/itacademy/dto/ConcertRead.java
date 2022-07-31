package com.itacademy.dto;

import com.itacademy.dao.entity.EventStatus;
import java.time.OffsetDateTime;
import java.util.UUID;

public class ConcertRead {

  private UUID uuid;

  private OffsetDateTime dtCreate;

  private OffsetDateTime dtUpdate;

  private String title;

  private String description;

  private OffsetDateTime dtEvent;

  private OffsetDateTime dtEndOfSale;

  private String type;

  private EventStatus status;

  public ConcertRead() {
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

  public OffsetDateTime getDtEvent() {
    return dtEvent;
  }

  public void setDtEvent(OffsetDateTime dtEvent) {
    this.dtEvent = dtEvent;
  }

  public OffsetDateTime getDtEndOfSale() {
    return dtEndOfSale;
  }

  public void setDtEndOfSale(OffsetDateTime dtEndOfSale) {
    this.dtEndOfSale = dtEndOfSale;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public EventStatus getStatus() {
    return status;
  }

  public void setStatus(EventStatus status) {
    this.status = status;
  }
}
