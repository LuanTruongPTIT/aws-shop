package com.aws.account.ViewModel.ResponseModel;

import lombok.Builder;

@Builder
public record AccountVm(
    Long id,
    String first_name,
    String last_name,
    String email,
    String phone,
    String avatar) {
}
