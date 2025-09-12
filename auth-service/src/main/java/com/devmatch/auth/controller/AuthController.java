package com.devmatch.auth.controller;

import com.devmatch.auth.dto.AuthResponse;
import com.devmatch.auth.dto.LoginRequest;
import com.devmatch.auth.dto.RegisterRequest;
import com.devmatch.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public String register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) {
    return authService.register(request, response);
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public String login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
    return authService.login(request, response);
  }

  @PostMapping("/refresh")
  public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response){
    authService.refreshAccessToken(request, response);
  }

  @PostMapping("/logout")
  void logout(HttpServletResponse response){
    authService.logout(response);
  }
}
