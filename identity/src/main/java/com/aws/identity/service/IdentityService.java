package com.aws.identity.service;

import java.time.Duration;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.aws.identity.exception.AccountIsNotActive;
import com.aws.identity.exception.NotFoundException;
import com.aws.identity.exception.PasswordNotMatch;
import com.aws.identity.model.IdentityModel;
import com.aws.identity.model.IdentityRoleModel;
import com.aws.identity.model.RoleModel;
import com.aws.identity.model.mapper.IdentitySyncDataMapper;
import com.aws.identity.publisher.IdentityEventPublisher;
import com.aws.identity.repository.IdentityRepository;
import com.aws.identity.utils.ComparePassword;
import com.aws.identity.utils.JwtTokenUtil;
import com.aws.identity.view.IdentityRoleView;
import com.aws.identity.view.TokenPair;
import com.aws.identity.view.TokenPayload;
import com.aws.identity.view.TokenSigning;
import com.aws.identity.view.QueuePayload.SyncAccountStatusPayload;
import com.aws.identity.view.RequestModel.LoginAccountDto;
import com.aws.identity.view.RequestModel.VerifyAccountDto;
import com.aws.identity.view.ResponseModel.LoginAccountResponse;
import com.aws.identity.view.consumer.AccountSync;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Service
@Transactional
public class IdentityService implements UserDetailsService {
  private final IdentitySyncDataMapper identitySyncDataMapper;
  private final IdentityRepository identityRepository;
  private final RedisService redisService;
  private final IdentityEventPublisher identityEventPublisher;
  private final RoleService roleService;
  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final JwtTokenUtil jwtTokenUtil;

  public IdentityService(
      IdentityRepository identityRepository,
      IdentitySyncDataMapper identitySyncDataMapper,
      RedisService redisService,
      IdentityEventPublisher identityEventPublisher,
      RoleService roleService,
      NamedParameterJdbcTemplate jdbcTemplate,
      JwtTokenUtil jwtTokenUtil

  ) {
    this.identitySyncDataMapper = identitySyncDataMapper;
    this.identityRepository = identityRepository;
    this.redisService = redisService;
    this.identityEventPublisher = identityEventPublisher;
    this.roleService = roleService;
    this.jdbcTemplate = jdbcTemplate;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  public void SyncDataIdentity(AccountSync accountSync) {
    IdentityModel identityModel = identitySyncDataMapper.toIdentityModellFromIdentitySyncData(accountSync);
    identityRepository.save(identityModel);
  }

  public boolean VerifyAccount(VerifyAccountDto request) {
    // get Key in redis
    StringBuilder cmd = new StringBuilder();
    cmd.append(request.jti());
    cmd.append("::");
    cmd.append(request.code());
    redisService.getValueString(cmd.toString()).orElseThrow(() -> new NotFoundException("INVALID_CODE"));

    // find JTI
    IdentityModel identityModel = findUserByJTI(request.jti());
    // find role
    RoleModel roleModel = roleService.GetRoleByName("user");
    identityModel.set_active(true);
    identityModel.set_verify(true);
    identityRepository.updateIdentityModel(identityModel.is_active(), identityModel.getJti(),
        identityModel.is_verify());
    // insert relation ship between role and identity
    IdentityRoleModel identityRoleModel = new IdentityRoleModel();
    identityRoleModel.setId_identity(identityModel.getId());
    identityRoleModel.setId_role(roleModel.getId());
    roleService.InsertRoleAccount(identityRoleModel);

    SyncAccountStatusPayload syncAccountStatusPayload = new SyncAccountStatusPayload(
        identityModel.getAccount_id(),
        identityModel.getJti(),
        identityModel.getId(),
        identityModel.is_active());

    // identityRoleModel.builder()

    identityEventPublisher.sendSyncAccountStatus(syncAccountStatusPayload);

    // delete key in redis
    redisService.deleteValueString(cmd.toString());
    return true;
  }

  public ResponseEntity<LoginAccountResponse> LoginAccount(LoginAccountDto request) {
    IdentityModel identityModel = identityRepository.findAccountByEmail(request.email())
        .orElseThrow(() -> new NotFoundException("INVALID_ACCOUNT"));
    if (!ComparePassword.checkPassword(request.password(), identityModel.getPassword())) {
      throw new PasswordNotMatch("Password does not match");
    }
    validateAccountStatus(identityModel);
    // find role by name role
    RoleModel roleModel = roleService.GetRoleByName("user");

    TokenPayload tokenPayload = createTokenPayload(identityModel, roleModel.getId());

    TokenPair tokens = generateTokenPair(tokenPayload);

    jwtTokenUtil.storeTokensInRedis(identityModel.getJti(), tokens);

    LoginAccountResponse response = LoginAccountResponse.builder()
        .accessToken(
            tokens.getAccessToken().getToken())
        .refreshToken(tokens.getRefreshToken().token)
        .expiresInAccessToken(tokens.getAccessToken().expiryDate.toString())
        .expiresInRefreshToken(tokens.getRefreshToken().expiryDate.toString())
        .build();

    return ResponseEntity.ok(response);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    IdentityModel indentityModel = findUserByJTI(username);
    return User.builder()
        .username(indentityModel.getEmail())
        .password(indentityModel.getPassword())
        .disabled(indentityModel.is_active())
        .accountLocked(indentityModel.is_verify())
        .build();

  }

  public UserDetails loadUserByJTI(String jti) throws UsernameNotFoundException {
    IdentityModel identityModel = findUserByJTI(jti);
    return loadUserByUsername(identityModel.getEmail());
  }

  private IdentityModel findUserByJTI(String jti) {
    IdentityModel identityModel = identityRepository.findByJti(jti)
        .orElseThrow(() -> new NotFoundException("INVALID_JTI"));
    return identityModel;
  }

  // private Optional<IdentityRoleView> GetAccountByJTI(String jti) {
  // StringBuilder sql = new StringBuilder("""
  // SELECT id_identity, account_id, jti, email, password, is_deleted, is_active,
  // is_ban, is_verify
  // FROM table_identity_role as iden_role
  // LEFT JOIN table_identity as iden
  // ON iden_role.id_identity = iden.
  // LEFT JOIN table_role as role
  // ON iden_role.id_role = role.id
  // WHERE jti = :jti
  // """);
  // }
  private void validateAccountStatus(IdentityModel identityModel) {
    if (!identityModel.is_active()) {
      throw new AccountIsNotActive("ACCOUNT_IS_NOT_ACTIVE");
    }
    if (!identityModel.is_verify()) {
      throw new AccountIsNotActive("ACCOUNT_IS_NOT_VERIFY");
    }
    if (identityModel.is_ban()) {
      throw new AccountIsNotActive("ACCOUNT_IS_BAN");
    }
    if (identityModel.is_deleted()) {
      throw new AccountIsNotActive("ACCOUNT_IS_DELETED");
    }
  }

  private TokenPayload createTokenPayload(IdentityModel identity, Long role) {
    return new TokenPayload(
        identity.getJti(),
        "http://aws", // Consider making these configurable
        "http://aws",
        role);
  }

  private TokenPair generateTokenPair(TokenPayload payload) {
    return new TokenPair(
        jwtTokenUtil.generateToken(payload, true),
        jwtTokenUtil.generateToken(payload, false));
  }
}
