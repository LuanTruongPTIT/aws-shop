package com.aws.webhook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aws.webhook.model.Recipients;

@Repository
public interface RecipientRepository extends JpaRepository<Recipients, Long> {

}
