package com.aws.account.ViewModel.RequestModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendEmailVerificationDto(
    @Email @NotBlank(message = "Email is required") String email,
    @NotBlank(message = "Jti is required") String jti) {
}
