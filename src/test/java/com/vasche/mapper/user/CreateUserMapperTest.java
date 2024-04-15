package com.vasche.mapper.user;

import com.vasche.dto.user.CreateUserDto;
import com.vasche.entity.Role;
import com.vasche.entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CreateUserMapperTest {
    private final CreateUserMapper mapper = CreateUserMapper.getInstance();

    @Test
    void map() {
        CreateUserDto dto = CreateUserDto.builder()
                .email("schebetovskiyvadim@gmail.com")
                .firstName("Vadim")
                .lastName("Schebetovskiy")
                .password("lol123!@")
                .role(Role.CLIENT.name())
                .build();

        User actualResult = mapper.mapFrom(dto);

        User expectedResult = User.builder()
                .email("schebetovskiyvadim@gmail.com")
                .firstName("Vadim")
                .lastName("Schebetovskiy")
                .password("lol123!@")
                .role(Role.CLIENT)
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
