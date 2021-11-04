package com.disqo.assignment.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
  private final int httpStatus;

  public CustomException(int httpStatus) {
    super();
    this.httpStatus = httpStatus;
  }

  public CustomException(String message, int httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public CustomException(String message, Throwable cause, int httpStatus) {
    super(message, cause);
    this.httpStatus = httpStatus;
  }

  public CustomException(Throwable cause, int httpStatus) {
    super(cause);
    this.httpStatus = httpStatus;
  }
}
