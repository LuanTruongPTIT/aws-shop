package com.aws.identity.model;

import com.aws.identity.listener.CustomAuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "identity_role")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EntityListeners(CustomAuditingEntityListener.class)
public class IdentityRoleModel extends AbstractAuditEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "id_identity", nullable = false)
  private int id_identity;

  @Column(name = "id_role", nullable = false)
  private int id_role;
}
