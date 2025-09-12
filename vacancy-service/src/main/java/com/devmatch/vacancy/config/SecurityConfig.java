package com.devmatch.vacancy.config;

import com.devmatch.vacancy.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthFilter;

  public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
    this.jwtAuthFilter = jwtAuthFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.GET, "/api/vacancies/**").permitAll()
//            .hasAnyRole("RECRUITER", "ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/vacancies").authenticated()
//            .hasAnyRole("RECRUITER", "ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/vacancies/**").authenticated()
//            .hasAnyRole("RECRUITER", "ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/vacancies/**").authenticated()
//            .hasAnyRole("RECRUITER", "ADMIN")
                .requestMatchers("/api/vacancies/mine").hasRole("RECRUITER")
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}