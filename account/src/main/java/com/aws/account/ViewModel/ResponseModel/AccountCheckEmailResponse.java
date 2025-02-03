package com.aws.account.ViewModel.ResponseModel;

import java.util.Optional;

import lombok.Builder;

@Builder
public record AccountCheckEmailResponse(
    Boolean exist,
    Optional<AccountVm> user) {

}
