package com.aws.identity.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class IdentityRoleView {
  public Long id_identity;
  public Long account_id;
  public String jti;
  public String email;
  public String password;
  public boolean is_deleted;
  public boolean is_active;
  public boolean is_ban;
  public boolean is_verify;
  public String role;
  public int id_role;
}
