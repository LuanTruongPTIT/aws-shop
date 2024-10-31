package com.aws.account.ViewModel.QueuePayload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountSyncStatusConsumer {
  public Long id;
  public String jti;
  public boolean is_active;
  public Long id_identity;
}
