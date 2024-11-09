package com.aws.identity.model;

import com.aws.identity.listener.CustomAuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "identity")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(CustomAuditingEntityListener.class)
public class IdentityModel extends AbstractAuditEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "account_id", nullable = false)
  private Long account_id;

  @Column(name = "jti", nullable = false)
  private String jti;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
  private boolean is_deleted;

  @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
  private boolean is_active;

  @Column(name = "is_ban", nullable = false, columnDefinition = "boolean default false")
  private boolean is_ban;

  @Column(name = "is_verify", nullable = false, columnDefinition = "boolean default false")
  private boolean is_verify;
}
