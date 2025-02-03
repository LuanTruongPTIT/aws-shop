package com.aws.spring_cloud_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Configuration;
import com.aws.spring_cloud_gateway.authentication.JwtAuthenticationFilter;

@Configuration
public class GatewayConfig {
  public RouteLocator customLocator(RouteLocatorBuilder builder) {
    return builder.routes().route("account-service", r -> r.path(
        "/api/v1/account/**")
        .filters(f -> f.filter(new JwtAuthenticationFilter().apply(new JwtAuthenticationFilter.Config())))
        .uri("lb://account-service")).build();
  }
}
