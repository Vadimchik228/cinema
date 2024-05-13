package com.vasche.mapper.seat;

import com.vasche.dto.seat.SeatAllDataDto;
import com.vasche.entity.Seat;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SeatAllDataMapperTest {
    private final SeatAllDataMapper mapper = new SeatAllDataMapper();

    @Test
    void map() {
        Seat seat = Seat.builder()
                .id(1)
                .lineId(1)
                .number(1)
                .build();

        SeatAllDataDto actualResult = mapper.mapFrom(seat, true);

        SeatAllDataDto expectedResult = SeatAllDataDto.builder()
                .id(1)
                .lineId(1)
                .number(1)
                .isReserved(true)
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
