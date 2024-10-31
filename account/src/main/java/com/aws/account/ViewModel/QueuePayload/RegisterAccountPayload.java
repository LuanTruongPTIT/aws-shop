package com.aws.account.ViewModel.QueuePayload;

import lombok.Builder;

@Builder
public record RegisterAccountPayload(
    String email,
    String body,
    String subject,
    String id) {
}