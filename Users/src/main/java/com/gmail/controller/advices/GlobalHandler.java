package com.gmail.controller.advices;

import com.gmail.service.custom_exception.multiple.EachErrorDefinition;
import com.gmail.service.custom_exception.multiple.ErrorsDefinition;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.ErrorDefinition;
import com.gmail.service.custom_exception.single.SingleException;
import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path.Node;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@RestControllerAdvice
public class GlobalHandler {

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorsDefinition handle(Multiple400Exception e){
    return e.getErrorsDefinition();
  }

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

  @ExceptionHandler({HttpMessageNotReadableException.class,
      MethodArgumentTypeMismatchException.class, SingleException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<ErrorDefinition> handle(){
    SingleException singleException = new SingleException();
    ErrorDefinition errorDefinition = new ErrorDefinition(
        "Invalid data. Change the request and send it again");
    singleException.getDescriptions().add(errorDefinition);

    return singleException.getDescriptions();
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public List<ErrorDefinition> handle(IllegalStateException e){
    SingleException singleException = new SingleException();
    ErrorDefinition errorDefinition = new ErrorDefinition(
        "The server was unable to process the request correctly. Please contact the administrator");
    singleException.getDescriptions().add(errorDefinition);

    return singleException.getDescriptions();
  }

}
