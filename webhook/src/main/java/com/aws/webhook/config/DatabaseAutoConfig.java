package com.aws.webhook.config;

import java.util.Optional;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaRepositories("com.aws.webhook.repository")
@EntityScan("com.aws.webhook.model")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class DatabaseAutoConfig {
  @Bean
  public AuditorAware<String> auditorAware() {
    return () -> {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth == null) {
        // Optional trong Java là một lớp được giới thiệu từ Java 8, đại diện cho một
        // container có thể
        // chứa hoặc không chứa một giá trị. Nó giúp giảm thiểu việc phải kiểm tra null
        // và tránh lỗi
        // NullPointerException khi làm việc với giá trị không tồn tại.
        return Optional.of("");
      } else {
        return Optional.of(auth.getName());
      }
    };
  }
}
