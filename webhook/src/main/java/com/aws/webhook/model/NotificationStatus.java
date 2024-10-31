package com.aws.webhook.model;

import com.aws.webhook.model.enums.NotificationStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notification_status")
@Getter
@Setter
@NoArgsConstructor
public class NotificationStatus extends AbstractAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "recipient_id", nullable = false)
  private Long recipient_id;

  @Column(name = "status")
  private NotificationStatusEnum status; // 1 = success, 2 = failed
}
