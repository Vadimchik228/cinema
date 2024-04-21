package com.vasche.mapper.line;

import com.vasche.dto.line.LineDto;
import com.vasche.entity.Line;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LineMapperTest {
    private final LineMapper mapper = new LineMapper();

    @Test
    void map() {
        Line line = Line.builder()
                .id(1)
                .hallId(1)
                .number(1)
                .build();

        LineDto actualResult = mapper.mapFrom(line);

        LineDto expectedResult = LineDto.builder()
                .id(1)
                .hallId(1)
                .number(1)
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
