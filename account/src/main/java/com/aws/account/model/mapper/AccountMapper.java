package com.aws.account.model.mapper;

import com.aws.account.ViewModel.RequestModel.RegisterAccountDto;
import com.aws.account.ViewModel.RequestModel.SendEmailVerificationDto;
import com.aws.account.model.AccountModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

  @Mapping(target = "first_name", source = "first_name")
  @Mapping(target = "last_name", source = "last_name")
  @Mapping(target = "email", source = "email")
  @Mapping(target = "password", source = "password")
  @Mapping(target = "phone", source = "phone")
  AccountModel toAccountFromAccountRegisterDto(RegisterAccountDto registerAccountDto);

  @Mapping(target = "jti", source = "jti")
  @Mapping(target = "email", source = "email")
  AccountModel toAccountFromSendEmailVerificationDto(SendEmailVerificationDto sendEmailVerificationDto);
}
