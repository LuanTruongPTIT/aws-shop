package com.aws.account.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.aws.account.ViewModel.QueuePayload.RegisterAccountPayload;
import com.aws.account.ViewModel.RequestModel.RegisterAccountDto;
import com.aws.account.ViewModel.ResponseModel.AccountVm;
import com.aws.account.exception.CheckExistException;
import com.aws.account.model.AccountModel;
import com.aws.account.model.mapper.AccountMapper;
import com.aws.account.publisher.account.AccountEventPublisher;
import com.aws.account.repository.AccountRepository;
import com.aws.account.utils.Constants;
import com.aws.account.utils.HasingBcryptPassword;
import com.aws.account.utils.JtiGenerator;
import com.aws.account.utils.MessageUtils;
import com.aws.account.utils.SecureRandomNumberGenerator;

@Service
// @Transactional
public class AccountService {
  private final AccountRepository accountRepository;
  private final AccountMapper accountMapper;
  private final AccountEventPublisher accountEventPublisher;
  private final RedisService redisService;

  public AccountService(AccountRepository accountRepository, AccountMapper accountMapper,
      AccountEventPublisher accountEventPublisher, RedisService redisService) {
    this.accountRepository = accountRepository;
    this.accountMapper = accountMapper;
    this.accountEventPublisher = accountEventPublisher;
    this.redisService = redisService;

  }

  public AccountVm RegisterAccount(RegisterAccountDto registerAccountDto) {
    AccountModel accountModel = accountMapper.toAccountFromAccountRegisterDto(registerAccountDto);
    boolean checkEmail = accountRepository.existsByEmail(registerAccountDto.email());
    if (checkEmail) {
      throw new CheckExistException(Constants.ErrorCode.ERROR_ACCOUNT_EXIST, registerAccountDto.email());
    }
    accountModel.setPassword(HasingBcryptPassword.hashPassword(registerAccountDto.password()));
    String jti = JtiGenerator.generateJti();
    accountModel.setJti(jti);
    AccountModel account = accountRepository.save(accountModel);

    String code = SecureRandomNumberGenerator.generateFiveDigitNumber();

    // create key and save code in redis
    String key = MessageUtils.createKey(jti, "::", code);

    redisService.setValueString(key, code, Duration.ofMinutes(15).toSeconds());

    RegisterAccountPayload registerAccountPayload = RegisterAccountPayload.builder()
        .body("Your verification code is " + code)
        .email(account.getEmail())
        .id(String.valueOf(account.getId()))
        .subject("Register Account")
        .build();

    accountEventPublisher.sendEmailRegisterEvent(registerAccountPayload);
    return AccountVm.builder()
        .id(account.getId())
        .first_name(account.getFirst_name())
        .last_name(account.getLast_name())
        .email(account.getEmail())
        .phone(account.getPhone())
        .avatar(account.getAvatar())
        .build();
  }
}
