package com.vasche.mapper.line;

import com.vasche.dto.line.LineAllDataDto;
import com.vasche.dto.seat.SeatAllDataDto;
import com.vasche.entity.Line;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LineAllDataMapperTest {
    private final LineAllDataMapper mapper = new LineAllDataMapper();

    @Test
    void map() {
        Line line = Line.builder()
                .id(1)
                .hallId(1)
                .number(1)
                .build();

        List<SeatAllDataDto> seats = new ArrayList<>();

        LineAllDataDto actualResult = mapper.mapFrom(line, seats);

        LineAllDataDto expectedResult = LineAllDataDto.builder()
                .id(1)
                .hallId(1)
                .number(1)
                .seats(seats)
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
