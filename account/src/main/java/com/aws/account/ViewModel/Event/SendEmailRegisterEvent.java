package com.aws.account.ViewModel.Event;

import org.springframework.context.ApplicationEvent;

import com.aws.account.ViewModel.QueuePayload.RegisterAccountPayload;

public class SendEmailRegisterEvent extends ApplicationEvent {
  private final RegisterAccountPayload registerAccountPayload;

  public SendEmailRegisterEvent(Object source, RegisterAccountPayload registerAccountPayload) {
    super(source);
    this.registerAccountPayload = registerAccountPayload;
    // TODO Auto  2-generated constructor stub
  }

  public RegisterAccountPayload getRegisterAccountPayload() {
    return registerAccountPayload;
  }
}
