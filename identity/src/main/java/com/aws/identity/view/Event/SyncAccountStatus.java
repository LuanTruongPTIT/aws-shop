package com.aws.identity.view.Event;

import org.springframework.context.ApplicationEvent;

import com.aws.identity.view.QueuePayload.SyncAccountStatusPayload;

public class SyncAccountStatus extends ApplicationEvent {
  private final SyncAccountStatusPayload syncAccountStatusPayload;

  public SyncAccountStatus(Object source, SyncAccountStatusPayload syncAccountStatusPayload) {
    super(source);
    this.syncAccountStatusPayload = syncAccountStatusPayload;
  }

  public SyncAccountStatusPayload getSyncAccountStatusPayload() {
    return syncAccountStatusPayload;
  }
}
