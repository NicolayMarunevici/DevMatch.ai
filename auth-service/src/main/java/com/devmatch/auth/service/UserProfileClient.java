package com.devmatch.auth.service;

import com.devmatch.auth.dto.CreateUserProfileRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserProfileClient {
  private final RestTemplate restTemplate;
  private final String userServiceUrl;
  private static final Logger log = LoggerFactory.getLogger(UserProfileClient.class);

  public UserProfileClient(RestTemplate restTemplate, @Value("${user-service.url}") String userServiceUrl) {
    this.restTemplate = restTemplate;
    this.userServiceUrl = userServiceUrl;
  }

  public void createProfile(CreateUserProfileRequest profile, String token) {
    try {
      HttpHeaders headers = new org.springframework.http.HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setBearerAuth(token);

      HttpEntity<CreateUserProfileRequest> entity = new HttpEntity<>(profile, headers);

      restTemplate.postForEntity(userServiceUrl + "/api/users", entity, Void.class);
    } catch (Exception e){
      log.warn("Failed to create profile in user-pservice", e);
    }
  }
}
