package com.aws.identity.view.RequestModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginAccountDto(
    @Email @NotBlank(message = "Email is required") String email,
    @NotBlank(message = "Password is required") String password) {

}
