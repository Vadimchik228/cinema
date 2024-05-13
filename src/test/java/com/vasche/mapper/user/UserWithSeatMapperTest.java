package com.vasche.mapper.user;

import com.vasche.dto.user.UserWithSeatDto;
import com.vasche.entity.Role;
import com.vasche.entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserWithSeatMapperTest {
    private final UserWithSeatMapper mapper = new UserWithSeatMapper();

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

        int seatNumber = 1;
        int reservationId = 1;
        UserWithSeatDto actualResult = mapper.mapFrom(user, seatNumber, reservationId);

        UserWithSeatDto expectedResult = UserWithSeatDto.builder()
                .id(1)
                .firstName("Vadim")
                .lastName("Schebetovskiy")
                .email("schebetovskiyvadim@gmail.com")
                .seatNumber(seatNumber)
                .reservationId(reservationId)
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
