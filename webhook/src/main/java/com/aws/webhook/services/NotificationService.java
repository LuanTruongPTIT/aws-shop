package com.aws.webhook.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aws.webhook.model.Notification;
import com.aws.webhook.model.NotificationStatus;
import com.aws.webhook.model.Recipients;
import com.aws.webhook.repository.NotificationRepository;
import com.aws.webhook.repository.NotificationStatusRepository;
import com.aws.webhook.repository.RecipientRepository;

@Service
public class NotificationService {

  @Autowired
  private NotificationRepository notificationRepository;

  @Autowired
  private RecipientRepository recipientRepository;

  @Autowired
  private NotificationStatusRepository notificationStatusRepository;

  public Notification saveNotification(Notification notification) {
    System.out.println("Notification Service save");
    Notification result = notificationRepository.save(notification);
    return result;
  }

  public Recipients saveRecipient(Recipients recipients) {
    System.out.println("Notification Service save");
    Recipients result = recipientRepository.save(recipients);
    return result;
  }

  public NotificationStatus saveNotificationStatus(NotificationStatus notificationStatus) {
    NotificationStatus result = notificationStatusRepository.save(notificationStatus);
    return result;
  }
}