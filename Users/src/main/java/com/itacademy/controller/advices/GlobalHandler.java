package com.itacademy.controller.advices;

import com.itacademy.service.custom_exception.multiple.EachErrorDefinition;
import com.itacademy.service.custom_exception.multiple.ErrorsDefinition;
import com.itacademy.service.custom_exception.single.ErrorDefinition;
import com.itacademy.service.custom_exception.single.SingleException;
import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path.Node;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@RestControllerAdvice
public class GlobalHandler {

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorsDefinition handle(ConstraintViolationException e) {
    List<EachErrorDefinition> errors = new ArrayList<>();

    String field = null;

    for (ConstraintViolation constraintViolation : e.getConstraintViolations()) {

      for (Node node : constraintViolation.getPropertyPath()) {
        field = node.getName();
      }
      errors.add(new EachErrorDefinition(field, constraintViolation.getMessage()));
    }
    return new ErrorsDefinition(errors);
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<ErrorDefinition> handle1(IllegalArgumentException e){
    SingleException singleException = new SingleException();
    singleException.getDescriptions().add(new ErrorDefinition(e.getMessage()));

    return singleException.getDescriptions();
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<ErrorDefinition> handle2(UsernameNotFoundException e){
    SingleException singleException = new SingleException();
    singleException.getDescriptions().add(new ErrorDefinition(e.getMessage()));

    return singleException.getDescriptions();
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<ErrorDefinition> handle3(DataIntegrityViolationException e){
    SingleException singleException = new SingleException();
    singleException.getDescriptions().add(new ErrorDefinition("Mail or nick is already exist"));

    return singleException.getDescriptions();
  }

  @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<ErrorDefinition> handle4(){
    SingleException singleException = new SingleException();
    singleException.getDescriptions().add(new ErrorDefinition
        ("The request contains invalid data. Change the request and send it again"));

    return singleException.getDescriptions();
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public List<ErrorDefinition> handle(RuntimeException e){
    SingleException singleException = new SingleException();
    singleException.getDescriptions().add(new ErrorDefinition(
        "The server was unable to process the request correctly. Please contact the administrator"));

    return singleException.getDescriptions();
  }
}
