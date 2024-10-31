package com.aws.webhook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aws.webhook.services.SmsService;
import com.aws.webhook.viewmodel.TestSMS;

@RestController
@RequestMapping("/api/v1/sms")
public class NotificationController {

  @Autowired
  private SmsService smsService;

  @PostMapping("/send")
  public ResponseEntity<String> sendSms(@RequestBody TestSMS testSMS) {
    String messageId = smsService.sendSms(testSMS.getPhoneNumber(), testSMS.getMessage());
    return ResponseEntity.ok("SMS sent with Message ID: " + messageId);
  }
}
