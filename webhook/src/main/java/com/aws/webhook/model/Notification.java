package com.aws.webhook.model;

import com.aws.webhook.model.enums.NotificationSenderType;
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
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
public class Notification extends AbstractAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title", nullable = true)
  private String title;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "sender_id", nullable = false)
  private Long sender_id;

  @Column(name = "sender_type", nullable = false)
  private NotificationSenderType sender_type;
}
