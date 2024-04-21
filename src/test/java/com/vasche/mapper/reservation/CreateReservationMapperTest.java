package com.vasche.mapper.reservation;

import com.vasche.dto.reservation.CreateReservationDto;
import com.vasche.entity.Reservation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CreateReservationMapperTest {
    private final CreateReservationMapper mapper = new CreateReservationMapper();

    @Test
    void map() {
        CreateReservationDto dto = CreateReservationDto.builder()
                .screeningId("1")
                .seatId("1")
                .userId("1")
                .build();

        Reservation actualResult = mapper.mapFrom(dto);

        Reservation expectedResult = Reservation.builder()
                .screeningId(1)
                .seatId(1)
                .userId(1)
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
