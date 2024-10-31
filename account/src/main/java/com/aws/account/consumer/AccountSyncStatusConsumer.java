package com.aws.account.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AccountSyncStatusConsumer {

  @KafkaListener(topics = "sync-account-status")
  public void onAccountSyncStatus(String payload) {
    ObjectMapper objectMapper = new ObjectMapper();

    AccountSyncStatusConsumer accountSyncStatusConsumer;
    try {
      accountSyncStatusConsumer = objectMapper.readValue(payload, AccountSyncStatusConsumer.class);
    } catch (Exception e) {
      e.printStackTrace();
      
    }
  }
}
