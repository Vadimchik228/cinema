package com.vasche.mapper.hall;

import com.vasche.dto.hall.HallDto;
import com.vasche.entity.Hall;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HallMapperTest {
    private final HallMapper mapper = HallMapper.getInstance();

    @Test
    void map() {
        Hall hall = Hall.builder()
                .id(1)
                .name("the best hall")
                .build();

        HallDto actualResult = mapper.mapFrom(hall);

        HallDto expectedResult = HallDto.builder()
                .id(1)
                .name("the best hall")
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
