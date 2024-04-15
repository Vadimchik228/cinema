package com.vasche.mapper.seat;

import com.vasche.dto.seat.CreateSeatDto;
import com.vasche.entity.Seat;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CreateSeatMapperTest {
    private final CreateSeatMapper mapper = CreateSeatMapper.getInstance();

    @Test
    void map() {
        CreateSeatDto dto = CreateSeatDto.builder()
                .lineId("1")
                .number("1")
                .build();

        Seat actualResult = mapper.mapFrom(dto);

        Seat expectedResult = Seat.builder()
                .number(1)
                .lineId(1)
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
