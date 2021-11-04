package com.disqo.assignment.controller.mapper;

import com.disqo.assignment.controller.dto.UserDTO;
import com.disqo.assignment.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {
  public UserDTO mapUserToUserDTO(User user) {
    return UserDTO.builder()
        .id(user.getId())
        .email(user.getEmail())
        .build();
  }
}
