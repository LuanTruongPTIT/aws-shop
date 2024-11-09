package com.aws.identity.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.aws.identity.view.TokenPayload;
import com.aws.identity.view.TokenSigning;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil {
  private final String JWT_SECRET = "aws-secret-identity";
  private final Long JWT_EXPIRATION = 604800000L;

  private SecretKey getSigningKey() {
    byte[] keyBytes = JWT_SECRET.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public TokenSigning generateToken(TokenPayload tokenPayload) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
    // AES, SHA, MD5, BCRYPT, SHA-1, HMAC
    String token = Jwts.builder()
        .claim("jti", tokenPayload.jti)
        .setSubject(tokenPayload.iss)
        .setAudience(tokenPayload.aud)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(
            getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
    return new TokenSigning(token, expiryDate);
  }

  public String getJti(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJwt(token)
        .getBody()
        .get("jti")
        .toString();
  }

  public boolean ValidateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
