package com.itacademy.dto;

import com.itacademy.dao.entity.EventStatus;
import java.time.LocalDate;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public class FilmCreateUpdate {

  @NotBlank
  private String title;

  @NotBlank
  private String description;

  @NotNull
  private EventStatus eventStatus;

  @NotNull
  private Long dt_event;

  @NotNull
  @Range(min = 1)
  private Long dt_end_of_sale;

  @NotBlank
  private UUID countryUuid;

  @NotNull
  @Range(min = 1)
  private Long duration;

  @NotNull
  @Range(min = 1970)
  private Long releaseYear;

  @NotNull
  private LocalDate releaseDate;

  public FilmCreateUpdate() {
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

  public EventStatus getEventStatus() {
    return eventStatus;
  }

  public void setEventStatus(EventStatus eventStatus) {
    this.eventStatus = eventStatus;
  }

  public Long getDt_event() {
    return dt_event;
  }

  public void setDt_event(Long dt_event) {
    this.dt_event = dt_event;
  }

  public Long getDt_end_of_sale() {
    return dt_end_of_sale;
  }

  public void setDt_end_of_sale(Long dt_end_of_sale) {
    this.dt_end_of_sale = dt_end_of_sale;
  }

  public UUID getCountryUuid() {
    return countryUuid;
  }

  public void setCountryUuid(UUID countryUuid) {
    this.countryUuid = countryUuid;
  }

  public void setDuration(Long duration) {
    this.duration = duration;
  }

  public void setReleaseYear(Long releaseYear) {
    this.releaseYear = releaseYear;
  }

  public Long getDuration() {
    return duration;
  }

  public Long getReleaseYear() {
    return releaseYear;
  }

  public LocalDate getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(LocalDate releaseDate) {
    this.releaseDate = releaseDate;
  }

}
