package com.devmatch.auth.service.impl;

import com.devmatch.auth.dto.LoginRequest;
import com.devmatch.auth.dto.RegisterRequest;
import com.devmatch.auth.dto.UserCreatedEvent;
import com.devmatch.auth.model.User;
import com.devmatch.auth.outbox.OutboxPublisher;
import com.devmatch.auth.outbox.OutboxService;
import com.devmatch.auth.repository.UserRepository;
import com.devmatch.auth.security.JwtTokenProvider;
import com.devmatch.auth.service.AuthService;
import com.devmatch.auth.service.UserDomainService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class AuthServiceImpl implements AuthService {
  private final UserRepository userRepository;
  private final UserDomainService userDomainService;
  private final OutboxService outboxService;
  private final OutboxPublisher outboxPublisher;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;

  private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

  private static final String TOPIC_USER_CREATED = "devmatch.user.created.v1";

  public AuthServiceImpl(UserRepository userRepository, UserDomainService userDomainService,
                         OutboxService outboxService, OutboxPublisher outboxPublisher,
                         AuthenticationManager authenticationManager,
                         JwtTokenProvider jwtTokenProvider) {
    this.userRepository = userRepository;
    this.userDomainService = userDomainService;
    this.outboxService = outboxService;
    this.outboxPublisher = outboxPublisher;
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Transactional
  @Override
  public String register(RegisterRequest request, HttpServletResponse response) {

    User user = userDomainService.createUser(request);

    String accessToken = jwtTokenProvider.generateAccessToken(user);
    String refreshToken = jwtTokenProvider.generateRefreshToken(user);

    addTokenCookies(response, accessToken, refreshToken);

    var roles = user.getRoles()
        .stream()
        .map(r -> r.getName().name())
        .collect(Collectors.toSet());

    var userCreatedEvent =
        new UserCreatedEvent(UUID.randomUUID(), Instant.now(), user.getId(),
            user.getEmail(),
            user.getFirstName(), user.getLastName(), roles);

      var outboxId = outboxService.saveEvent(TOPIC_USER_CREATED, user.getId().toString(), userCreatedEvent);

    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

      @Override
      public void afterCommit() {
        try {
          outboxPublisher.publishOne(outboxId);
        } catch (Exception ex) {
        }
      }
    });

    return "User has been registered successfully";
  }

  @Override
  public String login(LoginRequest request, HttpServletResponse response) {
    var auth = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
    authenticationManager.authenticate(auth);

    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("User not found"));

    String accessToken = jwtTokenProvider.generateAccessToken(user);
    String refreshToken = jwtTokenProvider.generateRefreshToken(user);

    addTokenCookies(response, accessToken, refreshToken);

    return "Login Successful";
  }

  @Override
  public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
    Cookie[] cookies = request.getCookies();

    if(cookies == null){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    String refreshToken =
        Arrays.stream(cookies).filter(c -> "refreshToken".equals(c.getName())).map(Cookie::getValue)
            .findFirst().orElse(null);

    if(refreshToken == null){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    try{
      Claims claims = jwtTokenProvider.parseSignedClaims(refreshToken);

      if(claims.getExpiration().before(new Date())){
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }

      long userId = Long.parseLong(claims.get("userId").toString());
      User user =
          userRepository.findById(userId)
              .orElseThrow(() -> new RuntimeException("User not found"));

      String newAccessToken = jwtTokenProvider.generateAccessToken(user);
      addTokenCookies(response, newAccessToken, refreshToken);

      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/json");
      response.getWriter().write("{\"message\": \"Access token refreshed\"}");
    } catch (JwtException | IOException e){
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

  @Override
  public void logout(HttpServletResponse response) {
    Cookie accessCookie = new Cookie("accessToken", null);
    accessCookie.setHttpOnly(true);
    accessCookie.setSecure(false);
    accessCookie.setPath("/");
    accessCookie.setMaxAge(0);

    Cookie refreshCookie = new Cookie("refreshToken", null);
    refreshCookie.setHttpOnly(true);
    refreshCookie.setSecure(false);
    refreshCookie.setPath("/");
    refreshCookie.setMaxAge(0);

    response.addCookie(accessCookie);
    response.addCookie(refreshCookie);

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");
    try {
      response.getWriter().write("{\"message\": \"Logged out successfully\"}");
    } catch (IOException e) {
      throw new RuntimeException("Failed to write logout response", e);
    }
  }

  private void addTokenCookies(HttpServletResponse response, String accessToken, String refreshToken){
    Cookie accessCookie = new Cookie("accessToken", accessToken);
    accessCookie.setHttpOnly(true);
    accessCookie.setSecure(false);
    accessCookie.setPath("/");
    accessCookie.setMaxAge(1800); // 30 min

    Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
    refreshCookie.setHttpOnly(true);
    refreshCookie.setSecure(false);
    refreshCookie.setPath("/");
    refreshCookie.setMaxAge(7 * 24 * 60 * 60);

    response.addCookie(accessCookie);
    response.addCookie(refreshCookie);
  }
}
