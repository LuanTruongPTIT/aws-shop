package com.aws.identity.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import com.aws.identity.view.Event.SyncAccountStatus;
import com.aws.identity.view.QueuePayload.SyncAccountStatusPayload;

@Component
public class IdentityEventPublisher {
  @Autowired
  ApplicationEventPublisher applicationEventPublisher;

  public void sendSyncAccountStatus(SyncAccountStatusPayload syncAccountStatusPayload) {
    applicationEventPublisher.publishEvent(new SyncAccountStatus(this, syncAccountStatusPayload));
  }
}
