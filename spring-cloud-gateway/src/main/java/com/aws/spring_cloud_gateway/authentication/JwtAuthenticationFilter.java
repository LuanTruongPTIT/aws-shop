package com.aws.spring_cloud_gateway.authentication;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {
  private List<String> excludeUrls;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;
  public static class Config {
  }

  public JwtAuthenticationFilter() {
    excludeUrls = new ArrayList<>();
    excludeUrls.add("/api/identity/login");
    excludeUrls.add("/api/v1/account/register");
  }

  @Override
  public GatewayFilter apply(Config config) {
   return ((exchange, chain) -> {
    ServerHttpRequest request = exchange.getRequest();
    if (isSecure(request)) {
       if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
         throw new RuntimeException("Mising authorization header");
       }
       String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
     if(authHeader != null && authHeader.startsWith("Bearer")) {
         String token = authHeader.substring(7);
        
     }
    }
   
   });
  }

  public Boolean isSecure(ServerHttpRequest request) {
    return excludeUrls.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));
  }
}
