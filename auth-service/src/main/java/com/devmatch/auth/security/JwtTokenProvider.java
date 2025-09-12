package com.devmatch.auth.security;

import com.devmatch.auth.controller.AuthController;
import com.devmatch.auth.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
  private final SecretKey secretKey;
  private final long accessTokenValidityInMs;
  private final long refreshTokenValidityInMs;

  private static final Logger log = LoggerFactory.getLogger(AuthController.class);


  public JwtTokenProvider(SecretKey secretKey,
                          @Value("${jwt.access-token-expiration}") long accessTokenValidityInMs,
                          @Value("${jwt.refresh-token-expiration}") long refreshTokenValidityInMs) {
    this.secretKey = secretKey;
    this.accessTokenValidityInMs = accessTokenValidityInMs;
    this.refreshTokenValidityInMs = refreshTokenValidityInMs;
  }

  public String generateAccessToken(User user){
    return generateToken(user, accessTokenValidityInMs);
  }

  public String generateRefreshToken(User user){
    return generateToken(user, refreshTokenValidityInMs);
  }

  public boolean validateToken(String token){
    try{
      Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException exception){
      log.warn("Invalid JWT token: {}", exception.getMessage());
      return false;
    }
  }

  private String generateToken(User user, long expirationMs){
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expirationMs);

    return Jwts.builder()
        .subject(user.getEmail())
        .claim("role", user.getRoles().stream()
            .map(role -> role.getName().name()).collect(
            Collectors.toList()))
        .claim("userId", user.getId())
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(secretKey)
        .compact();
  }

  public String getEmailFromToken(String token){
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public Claims parseSignedClaims(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
