package com.aws.identity.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.aws.identity.constant.Action;
import com.aws.identity.service.IdentityService;
import com.aws.identity.view.consumer.AccountSync;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Service
public class IdentitySyncData {
  @Autowired
  private IdentityService identityService;

  @KafkaListener(topics = "dbaccount.public.account")
  public void listen(ConsumerRecord<?, ?> consumerRecord) {
    try {
      if (consumerRecord != null) {
        System.out.println("Message received: " + consumerRecord.value());
        JsonObject keyObject = new Gson().fromJson((String) consumerRecord.key(), JsonObject.class);
        if (keyObject != null) {
          JsonObject valueObject = new Gson().fromJson((String) consumerRecord.value(), JsonObject.class);
          if (valueObject != null) {
            String action = String.valueOf(valueObject.get("op")).replaceAll("\"", "");
            AccountSync accountSync = new Gson().fromJson(valueObject.get("after").toString(), AccountSync.class);
            System.out.println("Action: " + action);
            switch (action) {
              case Action.CREATE:
                identityService.SyncDataIdentity(accountSync);
                break;
            }
          }
        }
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
