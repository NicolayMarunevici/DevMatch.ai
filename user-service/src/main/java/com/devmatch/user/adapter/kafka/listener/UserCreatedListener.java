package com.devmatch.user.adapter.kafka.listener;

import com.devmatch.user.dto.UserCreatedEvent;
import com.devmatch.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedListener {
  private final UserService userService;
  private final ObjectMapper objectMapper;

  Logger log = LoggerFactory.getLogger(UserCreatedListener.class);

  public UserCreatedListener(UserService userService, ObjectMapper objectMapper) {
    this.userService = userService;
    this.objectMapper = objectMapper;
  }

  @KafkaListener(topics = "devmatch.user.created.v1", groupId = "user-service")
  public void onUserCreated(String payload) throws JsonProcessingException {
    log.info("--- Received user.created: {}", payload);
    var event = objectMapper.readValue(payload, UserCreatedEvent.class);
    userService.createUserProfile(event);
  }
}


