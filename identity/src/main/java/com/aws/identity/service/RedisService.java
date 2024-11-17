package com.aws.identity.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

  private RedisTemplate<String, String> redisTemplateString;

  public RedisService(RedisTemplate<String, String> redisTemplateString) {
    this.redisTemplateString = redisTemplateString;
  }

  public Optional<String> getValueString(String key) {
    return Optional.ofNullable(redisTemplateString.opsForValue().get(key));
  }

  public void deleteValueString(String key) {
    redisTemplateString.delete(key);
  }

  public void setValueString(String key, String value, long ttlInSeconds) {
    redisTemplateString.opsForValue().set(key, value, ttlInSeconds, TimeUnit.SECONDS);
  }
}
