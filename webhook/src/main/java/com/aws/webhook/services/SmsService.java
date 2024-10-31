package com.aws.webhook.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Service
public class SmsService {
  @Autowired
  private SnsClient snsClient;

  public String sendSms(String phoneNumber, String message) {
    try {
      PublishRequest request = PublishRequest.builder()
          .message(message)
          .phoneNumber(phoneNumber)
          .build();
      PublishResponse result = snsClient.publish(request);
      System.out.println("PublishResponse: " + result);
      return result.messageId();
    } catch (Exception e) {
      return "Failed to send SMS";
    }
  }
}
