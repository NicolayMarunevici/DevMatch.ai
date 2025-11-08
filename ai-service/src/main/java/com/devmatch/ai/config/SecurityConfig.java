package com.devmatch.ai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http,
                                  @Value("${app.security.disabled:true}") boolean disabled) throws Exception {
    if (disabled) {
      http.csrf(csrf -> csrf.disable())
          .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
    } else {
      http.csrf(csrf -> csrf.disable())
          .authorizeHttpRequests(auth -> auth
              .requestMatchers("/actuator/health", "/actuator/info", "/actuator/prometheus").permitAll()
              .anyRequest().authenticated())
          .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()));
    }
    return http.build();
  }
}
