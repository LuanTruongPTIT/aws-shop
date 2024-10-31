package com.aws.identity.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aws.identity.view.RequestModel.VerifyAccountDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/identity")
public class IdentityController {

  @PostMapping("/verify-account")
  public boolean VerifyAccount(@RequestBody @Valid VerifyAccountDto request) {
    return true;
  }
}
