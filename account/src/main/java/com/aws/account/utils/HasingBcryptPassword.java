package com.aws.account.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HasingBcryptPassword {
  private static final BCryptPasswordEncoder BCrypt = new BCryptPasswordEncoder();

  public static String hashPassword(String password) {
    return BCrypt.encode(password);
  }

  public static boolean checkPassword(String password, String hashPassword) {
    return BCrypt.matches(password, hashPassword);
  }
}
