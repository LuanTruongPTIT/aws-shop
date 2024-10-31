package com.aws.identity.view.RequestModel;

import jakarta.validation.constraints.NotBlank;

public record VerifyAccountDto(
    @NotBlank(message = "Code is required") String code,
    @NotBlank(message = "Jti is required") String jti) {

}
