package com.brewless.order.exceptions;

import lombok.Getter;

@Getter
public class InvalidRequestException extends RuntimeException {

  private final String errorCode;

  public InvalidRequestException(String message, Throwable cause) {
    super(message, cause);
    this.errorCode = "OS00001";
  }

  public InvalidRequestException(String message) {
    super(message);
    this.errorCode = "OS00001";
  }
}