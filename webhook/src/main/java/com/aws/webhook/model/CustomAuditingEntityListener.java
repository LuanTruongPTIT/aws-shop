package com.aws.webhook.model;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.ObjectFactory;

@Configurable
public class CustomAuditingEntityListener extends AuditingEntityListener {
  public CustomAuditingEntityListener(ObjectFactory<AuditingHandler> auditingHandler) {
    super.setAuditingHandler(auditingHandler);
  }

  @PrePersist
  @Override
  public void touchForCreate(Object target) {
    AbstractAuditEntity abstractAuditEntity = (AbstractAuditEntity) target;
    if (abstractAuditEntity.getCreatedBy() == null) {
      super.touchForCreate(target);
    } else {
      if (abstractAuditEntity.getLastModifiedBy() == null) {
        abstractAuditEntity.setLastModifiedBy(abstractAuditEntity.getCreatedBy());
      }
    }
  }

  @PreUpdate
  @Override
  public void touchForUpdate(Object target) {
    AbstractAuditEntity abstractAuditEntity = (AbstractAuditEntity) target;
    if (abstractAuditEntity.getLastModifiedBy() == null) {

      super.touchForUpdate(target);
    }
  }
}
