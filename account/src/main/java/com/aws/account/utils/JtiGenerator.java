package com.aws.account.utils;

import java.util.UUID;

public class JtiGenerator {
  public static String generateJti() {
    long timestamp = System.currentTimeMillis();
    String randomUUID = UUID.randomUUID().toString();
    return timestamp + "-" + randomUUID;
  }
}
