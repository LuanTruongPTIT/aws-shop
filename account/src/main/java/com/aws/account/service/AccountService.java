package com.aws.account.service;

import java.time.Duration;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aws.account.ViewModel.QueuePayload.AccountSyncStatusConsumerPayload;
import com.aws.account.ViewModel.QueuePayload.RegisterAccountPayload;
import com.aws.account.ViewModel.RequestModel.RegisterAccountDto;
import com.aws.account.ViewModel.RequestModel.SendEmailVerificationDto;
import com.aws.account.ViewModel.ResponseModel.AccountCheckEmailResponse;
import com.aws.account.ViewModel.ResponseModel.AccountVm;
import com.aws.account.exception.CheckExistException;
import com.aws.account.exception.NotFoundException;
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

    StringBuilder cmd = new StringBuilder();
    cmd.append(jti);
    cmd.append("::");
    cmd.append(code);
    System.out.println("cmd : " + cmd.toString());
    redisService.setValueString(cmd.toString(), code, Duration.ofMinutes(15).toSeconds());

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

  public Boolean AccountSyncStatus(AccountSyncStatusConsumerPayload accountSyncStatusConsumer) {
    AccountModel accountModel = new AccountModel();
    accountModel.setJti(accountSyncStatusConsumer.getJti());
    accountModel.set_active(accountSyncStatusConsumer.is_active());
    accountModel.setId_identity(accountSyncStatusConsumer.getId_identity());
    accountRepository.updateAccountSyncStatus(accountSyncStatusConsumer.getId(), accountSyncStatusConsumer.is_active(),
        accountSyncStatusConsumer.getId_identity(), accountSyncStatusConsumer.getJti());
    return true;
  }

  public Boolean SendVerificationCode(SendEmailVerificationDto sendEmailVerificationDto) {
    AccountModel accountModel = accountMapper.toAccountFromSendEmailVerificationDto(sendEmailVerificationDto);
    // find by account is exist
    accountModel = accountRepository.findByJti(accountModel.getJti());

    if (accountModel == null) {
      throw new NotFoundException("Account is not exist");
    }
    String code = SecureRandomNumberGenerator.generateFiveDigitNumber();
    // create key and save code in redis
    String cmd = String.format("%s::%s", accountModel.getJti(), code);
    // logger.info("cmd : {}", cmd.toString());
    redisService.setValueString(cmd.toString(), code, Duration.ofMinutes(15).toSeconds());
    RegisterAccountPayload registerAccountPayload = RegisterAccountPayload.builder()
        .body("Your verification code is " + code)
        .email(accountModel.getEmail())
        .id(String.valueOf(accountModel.getId()))
        .subject("Send Verification Code")
        .build();

    accountEventPublisher.sendEmailRegisterEvent(registerAccountPayload);
    return true;
  }

  public AccountCheckEmailResponse CheckEmailIsExist(String email) {
    AccountModel accountModel = accountRepository.findByEmail(email);
    if (accountModel == null) {
      return AccountCheckEmailResponse.builder()
          .exist(false)
          .user(null)
          .build();
    }
    Optional<AccountVm> accountVm = Optional.ofNullable(AccountVm.builder().avatar(accountModel.getAvatar())
        .email(accountModel.getEmail())
        .first_name(accountModel.getFirst_name())
        .id(accountModel.getId())
        .last_name(accountModel.getLast_name())
        .phone(accountModel.getPhone())
        .build());
    return AccountCheckEmailResponse.builder()
        .exist(true)
        .user(
            accountVm)
        .build();
  }

  private void sendEmail(AccountModel accountModel, String code) {
    RegisterAccountPayload registerAccountPayload = RegisterAccountPayload.builder()
        .body("Your verification code is " + code)
        .email(accountModel.getEmail())
        .id(String.valueOf(accountModel.getId()))
        .subject("Send Verification Code")
        .build();

    accountEventPublisher.sendEmailRegisterEvent(registerAccountPayload);
  }
}
