package com.aws.identity.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

  private RedisTemplate<String, String> redisTemplateString;

  public RedisService(RedisTemplate<String, String> redisTemplateString) {
    this.redisTemplateString = redisTemplateString;
  }

  public String getValueString(String key) {
    return redisTemplateString.opsForValue().get(key);
  }

  public void deleteValueString(String key) {
    redisTemplateString.delete(key);
  }
}
