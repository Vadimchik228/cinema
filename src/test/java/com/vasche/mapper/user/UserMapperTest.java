package com.vasche.mapper.user;

import com.vasche.dto.user.UserDto;
import com.vasche.entity.Role;
import com.vasche.entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserMapperTest {
    private final UserMapper mapper = new UserMapper();

    @Test
    void map() {
        User user = User.builder()
                .id(1)
                .firstName("Vadim")
                .lastName("Schebetovskiy")
                .role(Role.CLIENT)
                .email("schebetovskiyvadim@gmail.com")
                .password("lol123!@")
                .build();

        UserDto actualResult = mapper.mapFrom(user);

        UserDto expectedResult = UserDto.builder()
                .id(1)
                .firstName("Vadim")
                .lastName("Schebetovskiy")
                .role(Role.CLIENT)
                .email("schebetovskiyvadim@gmail.com")
                .password("lol123!@")
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
