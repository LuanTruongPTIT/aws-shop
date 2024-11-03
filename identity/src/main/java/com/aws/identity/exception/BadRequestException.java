package com.aws.identity.exception;

import com.aws.identity.utils.MessageUtils;

public class BadRequestException extends RuntimeException {

  private String message;

  public BadRequestException(String errorCode, Object... var2) {
    this.message = MessageUtils.getMessage(errorCode, var2);
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
