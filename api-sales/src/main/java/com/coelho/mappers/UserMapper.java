package com.coelho.mappers;

import com.coelho.dtos.UserDTO;
import com.coelho.models.User;

public class UserMapper {

    private UserMapper() {
    }

    public static UserDTO toDTO(User user) {
        return new UserDTO(user.getId(),
                user.getName(),
                user.getEmail());
    }

    public static User toModel(UserDTO userDTO) {
        return new User(userDTO.getId(),
                userDTO.getName(),
                userDTO.getEmail(),
                null);
    }
}