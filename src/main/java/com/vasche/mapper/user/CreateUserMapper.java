package com.vasche.mapper.user;

import com.vasche.dto.user.CreateUserDto;
import com.vasche.entity.Role;
import com.vasche.entity.User;
import com.vasche.mapper.Mapper;

public class CreateUserMapper implements Mapper<CreateUserDto, User> {

    public CreateUserMapper() {
    }

    @Override
    public User mapFrom(CreateUserDto createUserDto) {
        return User.builder()
                .firstName(createUserDto.getFirstName())
                .lastName(createUserDto.getLastName())
                .email(createUserDto.getEmail())
                .password(createUserDto.getPassword())
                .role(Role.valueOf(createUserDto.getRole()))
                .build();
    }

}
