package com.disqo.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EntityNotFoundException extends CustomException {
  public EntityNotFoundException(String message) {
    super(message, HttpStatus.BAD_REQUEST.value());
  }
}
