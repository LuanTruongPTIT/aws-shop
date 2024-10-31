package com.aws.identity.view.consumer;

import lombok.Builder;

@Builder
public record AccountSync(Long id,
    String email,
    String password,
    String jti) {
}
