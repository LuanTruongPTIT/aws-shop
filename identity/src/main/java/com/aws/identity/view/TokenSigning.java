package com.aws.identity.view;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenSigning {
  public String token;
  public Date expiryDate;
}
