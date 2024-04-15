package com.vasche.dto.user;

import com.vasche.entity.Role;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDto {
    Integer id;
    String firstName;
    String lastName;
    String email;
    String password;
    Role role;
}
