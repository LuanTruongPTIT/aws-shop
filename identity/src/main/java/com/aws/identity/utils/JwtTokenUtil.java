package com.aws.identity.utils;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.aws.identity.service.RedisService;
import com.aws.identity.view.TokenPair;
import com.aws.identity.view.TokenPayload;
import com.aws.identity.view.TokenSigning;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil {
  private final String JWT_SECRET_ACCESSTOKEN = "aws-secret-identity-access-token-with-security";
  private final String JWT_SECRET_REFRESHTOKEN = "aws-secret-identity-refresh-token-with-security";
  private final Long JWT_EXPIRATION_ACCESSTOKEN = 604800000L;
  private final Long JWT_EXPIRATION_REFRESHTOKEN = 2592000000L;
  private static final String TOKEN_SEPARATOR = "::";
  private static final String ACCESS_TOKEN_PREFIX = "ACCESS_TOKEN";
  private static final String REFRESH_TOKEN_PREFIX = "REFRESH_TOKEN";
  private final RedisService redisService;

  public JwtTokenUtil(RedisService redisService) {
    this.redisService = redisService;
  }

  private SecretKey getSigningKey(String secret) {
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public TokenSigning generateToken(TokenPayload tokenPayload, boolean isAccessToken) {
    String secret = isAccessToken ? JWT_SECRET_ACCESSTOKEN : JWT_SECRET_REFRESHTOKEN;
    Long exp = isAccessToken ? JWT_EXPIRATION_ACCESSTOKEN : JWT_EXPIRATION_REFRESHTOKEN;
    Date now = new Date();
    Date expiryDate = new Date(
        now.getTime() + exp);
    String token = Jwts.builder()
        .claim("jti", tokenPayload.jti)
        .claim("role", tokenPayload.role)
        .setSubject(tokenPayload.iss)
        .setAudience(tokenPayload.aud)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(
            getSigningKey(
                secret),
            SignatureAlgorithm.HS256)
        .compact();
    return new TokenSigning(token, expiryDate);
  }

  public String getJti(String token, boolean isAccessToken) {
    String secret = isAccessToken ? JWT_SECRET_ACCESSTOKEN : JWT_SECRET_REFRESHTOKEN;
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey(secret))
        .build()
        .parseClaimsJwt(token)
        .getBody()
        .get("jti")
        .toString();
  }

  public boolean ValidateToken(String token, boolean isAccessToken) {
    String secret = isAccessToken ? JWT_SECRET_ACCESSTOKEN : JWT_SECRET_REFRESHTOKEN;
    try {
      Jwts.parserBuilder().setSigningKey(getSigningKey(secret)).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public void storeTokensInRedis(String jti, TokenPair tokens) {
    String access_token = tokens.getAccessToken().getToken();
    String refresh_token = tokens.getRefreshToken().getToken();
    Date expirDate_AccessToken = tokens.getAccessToken().getExpiryDate();
    Date expirDate_RefreshToken = tokens.getRefreshToken().getExpiryDate();
    // Store access token
    String accessTokenKey = String.join(TOKEN_SEPARATOR,
        ACCESS_TOKEN_PREFIX,
        jti,
        tokens.getAccessToken().getToken());
    redisService.setValueString(
        accessTokenKey,
        access_token,
        Duration.ofMinutes(expirDate_AccessToken.getTime()).toSeconds());

    // Store refresh token
    String refreshTokenKey = String.join(TOKEN_SEPARATOR,
        REFRESH_TOKEN_PREFIX,
        jti,
        refresh_token);
    redisService.setValueString(
        refreshTokenKey,
        refresh_token,
        Duration.ofMinutes(expirDate_RefreshToken.getTime()).toSeconds());
  }

}
