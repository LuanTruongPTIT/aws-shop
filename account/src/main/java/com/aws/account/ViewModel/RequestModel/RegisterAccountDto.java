package com.aws.account.ViewModel.RequestModel;

import com.aws.account.validation.ValidatePhone;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterAccountDto(
    @NotBlank(message = "First name is required") @Size(min = 1, max = 100, message = "First name is required") String first_name,
    @NotBlank(message = "Last name is required") @Size(min = 1, max = 100, message = "Last name is required") String last_name,
    @Email @NotBlank(message = "Email is required") String email,
    @NotBlank @Size(min = 8, message = "Password is required minimum 8 characters") String password,
    @ValidatePhone String phone) {
}
