package com.devmatch.auth.service;

import com.devmatch.auth.dto.LoginRequest;
import com.devmatch.auth.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public interface AuthService {
  String register(RegisterRequest request, HttpServletResponse response);
  String login(LoginRequest request, HttpServletResponse response);
  void refreshAccessToken(HttpServletRequest request, HttpServletResponse response);
  void logout(HttpServletResponse response);
}