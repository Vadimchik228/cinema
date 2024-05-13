package com.vasche.dto.user;

import com.vasche.entity.Role;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class UserDto {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final Role role;
}
