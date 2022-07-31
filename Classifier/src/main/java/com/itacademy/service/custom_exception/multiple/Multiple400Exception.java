package com.itacademy.service.custom_exception.multiple;

public class Multiple400Exception extends Exception{

  private ErrorsDefinition errorsDefinition;

  public Multiple400Exception(ErrorsDefinition errorsDefinition) {
    this.errorsDefinition = errorsDefinition;
  }

  public ErrorsDefinition getErrorsDefinition() {
    return errorsDefinition;
  }
}
