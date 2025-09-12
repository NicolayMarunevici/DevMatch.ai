package com.devmatch.auth.service.impl;

import com.devmatch.auth.dto.AuthResponse;
import com.devmatch.auth.dto.CreateUserProfileRequest;
import com.devmatch.auth.dto.LoginRequest;
import com.devmatch.auth.dto.RegisterRequest;
import com.devmatch.auth.dto.RoleEnum;
import com.devmatch.auth.model.Provider;
import com.devmatch.auth.model.Role;
import com.devmatch.auth.model.User;
import com.devmatch.auth.repository.RoleRepository;
import com.devmatch.auth.repository.UserRepository;
import com.devmatch.auth.security.JwtTokenProvider;
import com.devmatch.auth.service.AuthService;
import com.devmatch.auth.service.UserProfileClient;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;

  private final UserProfileClient userProfileClient;

  private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

  public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                         PasswordEncoder passwordEncoder,
                         AuthenticationManager authenticationManager,
                         JwtTokenProvider jwtTokenProvider, UserProfileClient userProfileClient) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
    this.userProfileClient = userProfileClient;
  }

  @Override
  public String register(RegisterRequest request, HttpServletResponse response) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new RuntimeException("User already registered");
    }

    Set<RoleEnum> roleEnum = request.getRoles() != null ? request.getRoles() : Set.of(RoleEnum.ROLE_CANDIDATE);
    Set<Role> role = roleRepository.findByNameIn(roleEnum);

    if(role == null){
      throw new RuntimeException("Role not found: " + roleEnum);
    }

    User user = new User();
    user.setEmail(request.getEmail());
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRoles(role);
    user.setProvider(Provider.NONE);
    user.setEnabled(true);
    user.setLocked(false);

    userRepository.save(user);

    CreateUserProfileRequest profile = new CreateUserProfileRequest();
    profile.setId(user.getId());
    profile.setEmail(user.getEmail());
    profile.setFirstName(user.getFirstName());
    profile.setLastName(user.getLastName());
    profile.setRole(
        user.getRoles()
            .stream()
            .map(e -> e.getName().name())
            .collect(Collectors.toSet()));

    String accessToken = jwtTokenProvider.generateAccessToken(user);
    String refreshToken = jwtTokenProvider.generateRefreshToken(user);

    addTokenCookies(response, accessToken, refreshToken);

    try {
      userProfileClient.createProfile(profile, accessToken);
      System.out.println(">>> REGISTER CALLED");
    } catch (Exception e){
      log.warn("Failed to create profile in user-pservice", e);
    }

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
