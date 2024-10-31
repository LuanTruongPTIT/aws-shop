package com.aws.identity.service;

import org.springframework.stereotype.Service;

import com.aws.identity.model.IdentityModel;
import com.aws.identity.model.mapper.IdentitySyncDataMapper;
import com.aws.identity.publisher.IdentityEventPublisher;
import com.aws.identity.repository.IdentityRepository;
import com.aws.identity.view.QueuePayload.SyncAccountStatusPayload;
import com.aws.identity.view.RequestModel.VerifyAccountDto;
import com.aws.identity.view.consumer.AccountSync;

@Service
public class IdentityService {
  private final IdentitySyncDataMapper identitySyncDataMapper;
  private final IdentityRepository identityRepository;
  private final RedisService redisService;
  private final IdentityEventPublisher identityEventPublisher;

  public IdentityService(
      IdentityRepository identityRepository,
      IdentitySyncDataMapper identitySyncDataMapper,
      RedisService redisService,
      IdentityEventPublisher identityEventPublisher) {
    this.identitySyncDataMapper = identitySyncDataMapper;
    this.identityRepository = identityRepository;
    this.redisService = redisService;
    this.identityEventPublisher = identityEventPublisher;
  }

  public void SyncDataIdentity(AccountSync accountSync) {
    IdentityModel identityModel = identitySyncDataMapper.toIdentityModellFromIdentitySyncData(accountSync);
    identityRepository.save(identityModel);
    System.out.println("Data Synced");
  }

  public boolean VerifyAccount(VerifyAccountDto request) {
    // get Key in redis
    StringBuilder cmd = new StringBuilder();
    cmd.append(request.jti());
    cmd.append("::");
    cmd.append(request.code());
    String codeCheck = redisService.getValueString(cmd.toString());
    if (codeCheck == null) {
      return false;
    }
    // find JTI
    IdentityModel identityModel = identityRepository.findByJti(request.jti());
    if (identityModel == null) {
      return false;
    }
    identityModel.set_active(true);
    identityModel.set_verify(true);
    identityRepository.update(identityModel);
    SyncAccountStatusPayload syncAccountStatusPayload = new SyncAccountStatusPayload(
        identityModel.getAccount_id(),
        identityModel.getJti(),
        identityModel.getId(),
        identityModel.is_active());

    identityEventPublisher.sendSyncAccountStatus(syncAccountStatusPayload);
    return true;
  }
}
