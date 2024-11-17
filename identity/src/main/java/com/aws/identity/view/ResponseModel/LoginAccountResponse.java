package com.aws.identity.view.ResponseModel;

import lombok.Builder;

@Builder
public record LoginAccountResponse(
    String accessToken,
    String refreshToken,
    String expiresInAccessToken,
    String expiresInRefreshToken) {

}
