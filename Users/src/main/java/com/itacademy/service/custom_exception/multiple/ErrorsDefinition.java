package com.itacademy.service.custom_exception.multiple;

import java.util.ArrayList;
import java.util.List;

public class ErrorsDefinition {

  private final String logref = "structured_error";
  private List<EachErrorDefinition> errors = new ArrayList<>();

  public ErrorsDefinition(List<EachErrorDefinition> errors) {
    this.errors = errors;
  }

  public String getLogref() {
    return logref;
  }

  public List<EachErrorDefinition> getErrors() {
    return errors;
  }

}

