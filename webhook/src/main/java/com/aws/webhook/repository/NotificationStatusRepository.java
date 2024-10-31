package com.aws.webhook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aws.webhook.model.NotificationStatus;

@Repository
public interface NotificationStatusRepository extends JpaRepository<NotificationStatus, Long> {

}
