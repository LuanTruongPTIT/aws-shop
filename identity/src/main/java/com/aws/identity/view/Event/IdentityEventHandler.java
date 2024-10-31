package com.aws.identity.view.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.aws.identity.view.QueuePayload.SyncAccountStatusPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class IdentityEventHandler {
  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  public void syncAccountStatusEventHandler(SyncAccountStatus syncAccountStatus) {
    SyncAccountStatusPayload syncAccountStatusPayload = syncAccountStatus.getSyncAccountStatusPayload();
    try {
      String payloadJson = objectMapper.writeValueAsString(syncAccountStatusPayload);
      kafkaTemplate.send("sync-account-status", payloadJson);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}
