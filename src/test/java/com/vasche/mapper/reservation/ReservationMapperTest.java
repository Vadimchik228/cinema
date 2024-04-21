package com.vasche.mapper.reservation;

import com.vasche.dto.reservation.ReservationDto;
import com.vasche.entity.Reservation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ReservationMapperTest {
    private final ReservationMapper mapper = new ReservationMapper();

    @Test
    void map() {
        Reservation reservation = Reservation.builder()
                .id(1)
                .seatId(1)
                .screeningId(1)
                .userId(1)
                .build();

        ReservationDto actualResult = mapper.mapFrom(reservation);

        ReservationDto expectedResult = ReservationDto.builder()
                .id(1)
                .seatId(1)
                .screeningId(1)
                .userId(1)
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
