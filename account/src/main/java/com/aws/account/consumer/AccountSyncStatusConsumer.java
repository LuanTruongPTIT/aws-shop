package com.aws.account.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.aws.account.ViewModel.QueuePayload.AccountSyncStatusConsumerPayload;
import com.aws.account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AccountSyncStatusConsumer {
  private AccountService accountService;

  public AccountSyncStatusConsumer(AccountService accountService) {
    this.accountService = accountService;
  }

  @KafkaListener(topics = "sync-account-status")
  public void onAccountSyncStatus(String payload) {
    ObjectMapper objectMapper = new ObjectMapper();

    AccountSyncStatusConsumerPayload accountSyncStatusConsumerPayload;
    try {
      accountSyncStatusConsumerPayload = objectMapper.readValue(payload, AccountSyncStatusConsumerPayload.class);
      accountService.AccountSyncStatus(accountSyncStatusConsumerPayload);
    } catch (Exception e) {
      e.printStackTrace();

    }
  }
}
