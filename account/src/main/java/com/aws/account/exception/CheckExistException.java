package com.aws.account.exception;

import com.aws.account.utils.MessageUtils;

public class CheckExistException extends RuntimeException {
  private String message;

  public CheckExistException(String errorCode, Object... var2) {
    this.message = MessageUtils.getMessage(errorCode, var2);
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
