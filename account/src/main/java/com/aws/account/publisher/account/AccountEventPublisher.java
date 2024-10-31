package com.aws.account.publisher.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import com.aws.account.ViewModel.Event.SendEmailRegisterEvent;
import com.aws.account.ViewModel.QueuePayload.RegisterAccountPayload;

@Component
public class AccountEventPublisher {

  @Autowired
  ApplicationEventPublisher applicationEventPublisher;

  public void sendEmailRegisterEvent(RegisterAccountPayload registerAccountPayload) {
    applicationEventPublisher.publishEvent(new SendEmailRegisterEvent(this, registerAccountPayload));
  }
}
