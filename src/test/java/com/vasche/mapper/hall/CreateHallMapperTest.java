package com.vasche.mapper.hall;

import com.vasche.dto.hall.CreateHallDto;
import com.vasche.entity.Hall;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CreateHallMapperTest {
    private final CreateHallMapper mapper = CreateHallMapper.getInstance();

    @Test
    void map() {
        CreateHallDto dto = CreateHallDto.builder()
                .name("the best hall")
                .build();

        Hall actualResult = mapper.mapFrom(dto);

        Hall expectedResult = Hall.builder()
                .name("the best hall")
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
