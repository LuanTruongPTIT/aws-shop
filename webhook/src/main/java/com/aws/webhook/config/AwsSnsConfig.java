package com.aws.webhook.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class AwsSnsConfig {

  @Value("${aws_access_key_id}")
  private String accessKey;

  @Value("${aws_secret_access_key}")
  private String secretKey;

  @Bean
  public SnsClient snsClient() {
    AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

    return SnsClient.builder()
        .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
        .region(Region.AP_SOUTHEAST_1)
        .build();
  }
}
