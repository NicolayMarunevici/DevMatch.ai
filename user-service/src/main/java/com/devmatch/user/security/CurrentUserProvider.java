package com.devmatch.user.security;

import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import java.text.ParseException;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {
  private final HttpServletRequest request;

  private final JwtTokenProvider jwtTokenProvider;

  public CurrentUserProvider(HttpServletRequest request, JwtTokenProvider jwtTokenProvider) {
    this.request = request;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  public Long getCurrentUserId() {
    String token = jwtTokenProvider.resolveToken(request);

    if (token == null || token.isEmpty()) {
      throw new RuntimeException("Missing or invalid Authorization header");
    }

    try {
      SignedJWT jwt = (SignedJWT) JWTParser.parse(token);
      Object userIdClaim = jwt.getJWTClaimsSet().getClaim("userId");
      if (userIdClaim == null) {
        throw new RuntimeException("userId not found in token");
      }

      return Long.parseLong(userIdClaim.toString());
    } catch (ParseException | NumberFormatException e) {
      throw new RuntimeException("Invalid token", e);
    }
  }

}

