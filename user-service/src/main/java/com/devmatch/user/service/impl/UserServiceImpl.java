package com.devmatch.user.service.impl;

import com.devmatch.user.dto.CreateUserProfileRequest;
import com.devmatch.user.dto.UpdateProfileRequest;
import com.devmatch.user.dto.UserDto;
import com.devmatch.user.mapper.UserMapper;
import com.devmatch.user.model.User;
import com.devmatch.user.repository.UserRepository;
import com.devmatch.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @Override
  public void createUserProfile(CreateUserProfileRequest request) {
    User user = userRepository.findById(request.getId())
        .orElseGet(() -> {
          User newUser = new User();
          newUser.setId(request.getId());
          return newUser;
        });

    user.setEmail(request.getEmail());
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setRole(request.getRole());

    userRepository.save(user);
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
