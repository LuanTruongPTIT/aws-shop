package com.aws.account.publisher.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.aws.account.ViewModel.Event.SendEmailRegisterEvent;
import com.aws.account.ViewModel.QueuePayload.RegisterAccountPayload;

@Component
public class AccountEventHandler {
  private static final String topic_register_account = "register-account";

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @EventListener
  public void sendEmailRegisterEventHandler(SendEmailRegisterEvent event)
      throws InterruptedException {
    System.out.println("sendEmailRegisterEventHandler");
    RegisterAccountPayload registerAccountPayload = event.getRegisterAccountPayload();
    System.out.println(registerAccountPayload.toString());

    try {
      String payloadJson = objectMapper.writeValueAsString(registerAccountPayload);
      kafkaTemplate.send("register-account", payloadJson);
    } catch (JsonProcessingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
