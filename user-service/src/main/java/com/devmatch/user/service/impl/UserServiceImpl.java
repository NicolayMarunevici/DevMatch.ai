package com.devmatch.user.service.impl;

import com.devmatch.user.adapter.kafka.listener.UserCreatedListener;
import com.devmatch.user.dto.CreateUserProfileRequest;
import com.devmatch.user.dto.UpdateProfileRequest;
import com.devmatch.user.dto.UserCreatedEvent;
import com.devmatch.user.dto.UserDto;
import com.devmatch.user.mapper.UserMapper;
import com.devmatch.user.model.ProcessedEvents;
import com.devmatch.user.model.User;
import com.devmatch.user.repository.ProcessedEventRepository;
import com.devmatch.user.repository.UserRepository;
import com.devmatch.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

  Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  private final UserRepository userRepository;
  private final ProcessedEventRepository eventRepository;
  private final UserMapper userMapper;

  public UserServiceImpl(UserRepository userRepository, ProcessedEventRepository eventRepository,
                         UserMapper userMapper) {
    this.userRepository = userRepository;
    this.eventRepository = eventRepository;
    this.userMapper = userMapper;
  }

  @Transactional
  @Override
  public void createUserProfile(UserCreatedEvent event) {
    // idempotency through PK
    try {
      eventRepository.save(new ProcessedEvents(event.eventId(), Instant.now()));
    } catch (DataIntegrityViolationException dup) {
      logger.info("Event {} already processed by PK, skip", event.eventId());
      return;
    }

    User user = userRepository.findById(event.userId())
        .orElseGet(() -> {
          User newUser = new User();
          newUser.setId(event.userId());
          return newUser;
        });

    user.setEmail(event.email());
    user.setFirstName(event.firstName());
    user.setLastName(event.lastName());
    user.setRole(event.roles());

    try {
      userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      logger.warn("Email '{}' already taken for another user; eventId={}, userId={}",
          user.getEmail(), event.eventId(), event.userId(), e);
      return;
    }

    logger.info("User {} was saved in user-service DB", user.getId());
  }

  @Override
  public UserDto getUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    return userMapper.toDto(user);
  }

  @Override
  public UserDto updateUser(Long id, UserDto userDto) {
    User existing = userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    User updatedUser = userMapper.toEntity(userDto);
    updatedUser.setId(existing.getId());
    return userMapper.toDto(userRepository.save(updatedUser));
  }

  public UserDto updateProfile(Long userId, UpdateProfileRequest request){
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    if(request.getFirstName() != null){
      user.setFirstName(request.getFirstName());
    }

    if(request.getLastName() != null){
      user.setLastName(request.getLastName());
    }

    if(request.getEmail() != null){
      user.setEmail(request.getEmail());
    }

    userRepository.save(user);

    return userMapper.toDto(user);
  }

  @Override
  public void deleteUser(Long id) {
    if (!userRepository.existsById(id)) {
      throw new EntityNotFoundException("User not found with id: " + id);
    }
    userRepository.deleteById(id);
  }

  @Override
  public List<UserDto> getAllByRole(Set<String> role) {
    return userRepository.findByRolesIn(role)
        .stream()
        .map(userMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<UserDto> searchByTechStack(String tech) {
    return userRepository.findByTechStackContainingIgnoreCase(tech)
        .stream()
        .map(userMapper::toDto)
        .collect(Collectors.toList());
  }
}
