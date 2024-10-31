package com.aws.account.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  @Autowired
  private RedisTemplate<String, String> redisTemplateString;

  public void setValueObject(String key, Object value) {
    redisTemplate.opsForValue().set(key, value);
  }

  public Object getValueObject(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public void setValueString(String key, String value, long ttlInSeconds) {
    redisTemplateString.opsForValue().set(key, value, ttlInSeconds, TimeUnit.SECONDS);
  }

  public String getValueString(String key) {
    return redisTemplateString.opsForValue().get(key);
  }

}
