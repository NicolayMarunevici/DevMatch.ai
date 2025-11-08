package com.devmatch.user.service;

import com.devmatch.user.dto.CreateUserProfileRequest;
import com.devmatch.user.dto.UpdateProfileRequest;
import com.devmatch.user.dto.UserCreatedEvent;
import com.devmatch.user.dto.UserDto;
import java.util.List;
import java.util.Set;

public interface UserService {
  UserDto getUserById(Long id);
  UserDto updateUser(Long id, UserDto userDto);
  void deleteUser(Long id);
  List<UserDto> getAllByRole(Set<String> role);

  List<UserDto> searchByTechStack(String tech);
  void createUserProfile(UserCreatedEvent event);
  UserDto updateProfile(Long userId, UpdateProfileRequest request);
}
