package com.disqo.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AuthorizationException extends CustomException {

  public AuthorizationException(String message) {
    super(message, HttpStatus.FORBIDDEN.value());
  }
}
