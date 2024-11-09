package com.aws.identity.view.ResponseModel;

public record LoginAccountResponse(
    String accessToken,
    String refreshToken,
    String expiresInAccessToken,
    String expiresInRefreshToken) {

}
