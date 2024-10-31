package com.aws.identity.view.QueuePayload;

import lombok.Builder;

@Builder
public record SyncAccountStatusPayload(
    Long id,
    String jti,
    Long id_identity,
    boolean is_active) {

}
