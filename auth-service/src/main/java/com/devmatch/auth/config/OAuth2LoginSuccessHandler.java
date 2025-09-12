package com.devmatch.auth.config;

import com.devmatch.auth.model.User;
import com.devmatch.auth.repository.UserRepository;
import com.devmatch.auth.security.JwtTokenProvider;
import com.devmatch.auth.dto.UserPrincipal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtTokenProvider jwtTokenProvider;
  private final UserRepository userRepository;

  public OAuth2LoginSuccessHandler(JwtTokenProvider jwtTokenProvider,
                                   UserRepository userRepository) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.userRepository = userRepository;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication)
      throws IOException, ServletException {

    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    Optional<User> optionalUser = userRepository.findByEmail(userPrincipal.getUsername());

    User user = optionalUser.orElseThrow(() -> new RuntimeException("User not found"));

    String accessToken = jwtTokenProvider.generateAccessToken(user);
    String refreshToken = jwtTokenProvider.generateRefreshToken(user);

    response.setContentType("application/json");
    response.getWriter().write("{\"accessToken\": \"" + accessToken + "\", \"refreshToken\": \"" + refreshToken + "\"}");
  }
}
