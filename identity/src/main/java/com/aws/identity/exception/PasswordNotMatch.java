package com.aws.identity.exception;

public class PasswordNotMatch extends RuntimeException {
  public PasswordNotMatch(final String message) {
    super(message);
  }
}
