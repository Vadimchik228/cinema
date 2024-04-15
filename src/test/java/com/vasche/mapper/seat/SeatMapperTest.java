package com.vasche.mapper.seat;

import com.vasche.dto.seat.SeatDto;
import com.vasche.entity.Seat;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SeatMapperTest {
    private final SeatMapper mapper = SeatMapper.getInstance();

    @Test
    void map() {
        Seat seat = Seat.builder()
                .id(1)
                .lineId(1)
                .number(1)
                .build();

        SeatDto actualResult = mapper.mapFrom(seat);

        SeatDto expectedResult = SeatDto.builder()
                .id(1)
                .lineId(1)
                .number(1)
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
