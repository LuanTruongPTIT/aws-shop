package com.aws.webhook.viewmodel.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAccountPayloadConsumer {
  public String email;
  public String body;
  String subject;
  public String id;

}
