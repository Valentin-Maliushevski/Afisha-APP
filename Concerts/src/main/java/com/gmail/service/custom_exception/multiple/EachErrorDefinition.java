package com.gmail.service.custom_exception.multiple;

public class EachErrorDefinition {

  private String field;
  private String message;

  public EachErrorDefinition(String field, String message) {
    this.field = field;
    this.message = message;
  }

  public String getField() {
    return field;
  }

  public String getMessage() {
    return message;
  }
}
