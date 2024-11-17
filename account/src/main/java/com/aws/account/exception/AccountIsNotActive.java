package com.aws.account.exception;

public class AccountIsNotActive extends RuntimeException {
  public AccountIsNotActive(String message) {
    super(message);
  }
}
