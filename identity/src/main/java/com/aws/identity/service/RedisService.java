package com.aws.identity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
  @Autowired
  private RedisTemplate<String, String> redisTemplateString;

  public String getValueString(String key) {
    return redisTemplateString.opsForValue().get(key);
  }
}
