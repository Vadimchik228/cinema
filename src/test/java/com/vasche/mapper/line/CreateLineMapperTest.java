package com.vasche.mapper.line;

import com.vasche.dto.line.CreateLineDto;
import com.vasche.entity.Line;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CreateLineMapperTest {
    private final CreateLineMapper mapper = new CreateLineMapper();

    @Test
    void map() {
        CreateLineDto dto = CreateLineDto.builder()
                .hallId("1")
                .number("1")
                .build();

        Line actualResult = mapper.mapFrom(dto);

        Line expectedResult = Line.builder()
                .hallId(1)
                .number(1)
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
