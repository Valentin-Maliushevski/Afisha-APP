package com.itacademy.dto;

import javax.validation.constraints.NotBlank;

public class CategoryCreate {

  @NotBlank
  private String title;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
