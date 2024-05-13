package com.vasche.mapper.hall;

import com.vasche.dto.hall.HallAllDataDto;
import com.vasche.dto.hall.HallDto;
import com.vasche.dto.line.LineAllDataDto;
import com.vasche.entity.Hall;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HallAllDataMapperTest {
    private final HallAllDataMapper mapper = new HallAllDataMapper();

    @Test
    void map() {
        Hall hall = Hall.builder()
                .id(1)
                .name("the best hall")
                .build();
        final List<LineAllDataDto> lines = new ArrayList<>();

        HallAllDataDto actualResult = mapper.mapFrom(hall, lines);

        HallAllDataDto expectedResult = HallAllDataDto.builder()
                .id(1)
                .name("the best hall")
                .lines(List.of())
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
