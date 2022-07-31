package com.itacademy.dao.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table( name = "film", schema = "film_events")
public class Film implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private UUID uuid;

  @Column(name = "dt_create")
  private OffsetDateTime dtCreate;

  @Version
  @Column(name = "dt_update")
  private OffsetDateTime dtUpdate;

  private String title;

  private String description;

  @Column(name = "dt_event")
  private OffsetDateTime dtEvent;

  @Column(name = "dt_end_of_sale")
  private OffsetDateTime dtEndOfSale;

  @Enumerated(EnumType.STRING)
  @Column(name = "event_status")
  private EventStatus eventStatus;

  @Column(name = "event_type")
  private String eventType;

  @Column(name = "country_uuid")
  private UUID countryUuid;

  private Long duration;

  @Column(name = "release_year")
  private Long releaseYear;

  @Column(name = "release_date")
  private LocalDate releaseDate;

  @Column(name = "author_uuid")
  private UUID authorUuid;

  public Film() {
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

  public EventStatus getEventStatus() {
    return eventStatus;
  }

  public void setEventStatus(EventStatus eventStatus) {
    this.eventStatus = eventStatus;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public UUID getCountryUuid() {
    return countryUuid;
  }

  public void setCountryUuid(UUID countryUuid) {
    this.countryUuid = countryUuid;
  }

  public Long getDuration() {
    return duration;
  }

  public void setDuration(Long duration) {
    this.duration = duration;
  }

  public Long getReleaseYear() {
    return releaseYear;
  }

  public void setReleaseYear(Long releaseYear) {
    this.releaseYear = releaseYear;
  }

  public LocalDate getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(LocalDate releaseDate) {
    this.releaseDate = releaseDate;
  }

  public UUID getAuthorUuid() {
    return authorUuid;
  }

  public void setAuthorUuid(UUID authorUuid) {
    this.authorUuid = authorUuid;
  }
}
