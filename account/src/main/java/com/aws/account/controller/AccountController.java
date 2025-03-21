package com.aws.account.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aws.account.ViewModel.RequestModel.RegisterAccountDto;
import com.aws.account.ViewModel.RequestModel.SendEmailVerificationDto;
import com.aws.account.ViewModel.ResponseModel.AccountVm;
import com.aws.account.service.AccountService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }
  @PostMapping("/register")
  public ResponseEntity<AccountVm> RegisterAccount(@RequestBody @Valid RegisterAccountDto registerAccountDto) {
    return ResponseEntity.ok(accountService.RegisterAccount(registerAccountDto));
  }

  @PostMapping("/send-verification-code")
  public boolean SendEmailVerificationCode(@RequestBody @Valid SendEmailVerificationDto sendEmailVerificationDto) {
    return accountService.SendVerificationCode(sendEmailVerificationDto);
  }

}
