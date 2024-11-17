package com.aws.identity.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenPair {
  TokenSigning accessToken;
  TokenSigning refreshToken;
}
