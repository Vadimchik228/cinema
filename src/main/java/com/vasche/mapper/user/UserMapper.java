package com.vasche.mapper.user;

import com.vasche.dto.user.UserDto;
import com.vasche.entity.User;
import com.vasche.mapper.Mapper;

public class UserMapper implements Mapper<User, UserDto> {

    public UserMapper() {
    }

    @Override
    public UserDto mapFrom(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .password(user.getPassword())
                .build();
    }

}
