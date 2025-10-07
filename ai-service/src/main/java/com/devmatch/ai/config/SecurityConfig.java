//package com.devmatch.ai.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//  @Bean
//  public SecurityFilterChain securityFilterChain(
//      HttpSecurity http,
//      @Value("${app.security.disabled:false}") boolean disabled) throws Exception {
//
////    if (disabled) {
//    http.csrf(csrf -> csrf.disable())
//        .authorizeHttpRequests(a -> a.anyRequest().permitAll());
//    return http.build();
////    }
////
////    http.csrf(csrf -> csrf.disable())
////        .authorizeHttpRequests(a -> a
////            .requestMatchers("/actuator/**").permitAll()
////            .anyRequest().authenticated())
////        .oauth2ResourceServer(o -> o.jwt(Customizer.withDefaults()));
////    return http.build();
////  }
//  }
//}
