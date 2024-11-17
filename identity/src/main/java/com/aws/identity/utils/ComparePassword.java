package com.aws.identity.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class ComparePassword {
  private static final BCryptPasswordEncoder BCrypt = new BCryptPasswordEncoder();

  public static boolean checkPassword(String password, String hashPassword) {
    return BCrypt.matches(password, hashPassword);
  }
}
