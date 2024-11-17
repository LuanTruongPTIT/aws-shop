package com.aws.account.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
// import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder.Default;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name", nullable = false)
  private String first_name;

  @Column(name = "last_name", nullable = false)
  private String last_name;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "phone", nullable = false)
  private String phone;

  @Column(name = "avatar")
  private String avatar;

  @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
  private boolean is_active;

  @Column(name = "id_identity", nullable = true)
  private Long id_identity;

  @Column(name = "jti", nullable = false, unique = true)
  private String jti;

  @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
  private boolean is_deleted;
  @CreationTimestamp
  private ZonedDateTime createdOn;
  @UpdateTimestamp
  private ZonedDateTime lastModifiedOn;
}