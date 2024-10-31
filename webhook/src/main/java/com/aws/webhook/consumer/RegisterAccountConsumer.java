package com.aws.webhook.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.aws.webhook.model.Notification;
import com.aws.webhook.model.NotificationStatus;
import com.aws.webhook.model.Recipients;
import com.aws.webhook.model.enums.NotificationSenderType;
import com.aws.webhook.model.enums.NotificationStatusEnum;
import com.aws.webhook.services.NotificationService;
import com.aws.webhook.services.SendEmailService;
import com.aws.webhook.viewmodel.payload.RegisterAccountPayloadConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RegisterAccountConsumer {

  @Autowired
  private SendEmailService sendEmailService;
  @Autowired
  private NotificationService notificationService;

  @KafkaListener(topics = "register-account")
  public void onRegisterAccount(String payload) {
    ObjectMapper objectMapper = new ObjectMapper();

    RegisterAccountPayloadConsumer registerAccountPayloadConsumer;
    try {
      registerAccountPayloadConsumer = objectMapper.readValue(payload,
          RegisterAccountPayloadConsumer.class);
      String subject = registerAccountPayloadConsumer.getSubject();

      Notification notification = new Notification();

      // save notification in database
      System.out.println("Notification set model");
      notification.setTitle(subject);
      notification.setContent(registerAccountPayloadConsumer.getBody());
      notification.setSender_type(NotificationSenderType.system);
      notification.setSender_id(Long.parseLong("0"));
      notification = notificationService.saveNotification(notification);

      // save recipient in database
      Recipients recipient = new Recipients();
      recipient.setNotification_id(notification.getId());
      recipient.setRecipient_id(Long.parseLong("0"));
      recipient = notificationService.saveRecipient(recipient);
      recipient = notificationService.saveRecipient(recipient);

      boolean ischeck = sendEmailService.sendEmail(registerAccountPayloadConsumer.getEmail(), subject,
          registerAccountPayloadConsumer.getBody());

      NotificationStatus notificationStatus = new NotificationStatus();
      notificationStatus.setRecipient_id(Long.parseLong(registerAccountPayloadConsumer.id));
      if (ischeck) {
        notificationStatus.setStatus(NotificationStatusEnum.Success);
        notificationService.saveNotificationStatus(notificationStatus);
      } else {
        notificationStatus.setStatus(NotificationStatusEnum.Failed);
        notificationService.saveNotificationStatus(notificationStatus);
      }
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}
