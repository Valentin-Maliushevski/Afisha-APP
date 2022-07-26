package com.gmail.service.custom_exception.single;

import java.util.ArrayList;
import java.util.List;

public class SingleException extends Exception{

  private List<ErrorDefinition> descriptions = new ArrayList<>();

  public List<ErrorDefinition> getDescriptions() {
    return descriptions;
  }

}
