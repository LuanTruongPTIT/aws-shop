package com.aws.identity.exception;

public class AccountIsNotActive extends RuntimeException {
  public AccountIsNotActive(String message) {
    super(message);
  }
}
