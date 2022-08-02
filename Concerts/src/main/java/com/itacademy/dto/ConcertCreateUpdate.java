package com.itacademy.dto;

import com.itacademy.dao.entity.EventStatus;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public class ConcertCreateUpdate {

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

  @NotNull
  private UUID categoryUuid;

  public ConcertCreateUpdate() {
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

  public UUID getCategoryUuid() {
    return categoryUuid;
  }

  public void setCategoryUuid(UUID categoryUuid) {
    this.categoryUuid = categoryUuid;
  }
}
