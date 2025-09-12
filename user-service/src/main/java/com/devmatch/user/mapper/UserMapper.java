package com.devmatch.user.mapper;

import com.devmatch.user.dto.UserDto;
import com.devmatch.user.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto toDto(User user);
  User toEntity(UserDto dto);
}
