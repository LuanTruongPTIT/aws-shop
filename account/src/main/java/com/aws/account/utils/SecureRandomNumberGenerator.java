package com.aws.account.utils;

import java.security.SecureRandom;

public class SecureRandomNumberGenerator {

  public static String generateFiveDigitNumber() {
    SecureRandom random = new SecureRandom();
    int number = random.nextInt(90000) + 10000;
    return String.valueOf(number);
  }
}