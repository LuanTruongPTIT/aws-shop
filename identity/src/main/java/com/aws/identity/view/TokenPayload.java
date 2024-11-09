package com.aws.identity.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class TokenPayload {
  public String jti;
  public String iss;
  public String aud;
}
