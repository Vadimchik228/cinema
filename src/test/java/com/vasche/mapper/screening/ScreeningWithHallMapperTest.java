package com.vasche.mapper.screening;

import com.vasche.dto.screening.ScreeningWithHallDto;
import com.vasche.entity.Hall;
import com.vasche.entity.Screening;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ScreeningWithHallMapperTest {
    private final ScreeningWithHallMapper mapper = new ScreeningWithHallMapper();

    @Test
    void map() {

        Screening screening = Screening.builder()
                .id(1)
                .price(BigDecimal.valueOf(200, 0))
                .startTime(LocalDateTime.of(2024, 1, 1, 16, 30))
                .movieId(1)
                .hallId(1)
                .build();

        Hall hall = Hall.builder()
                .id(1)
                .name("Hall1")
                .build();

        ScreeningWithHallDto actualResult = mapper.mapFrom(screening, hall);

        ScreeningWithHallDto expectedResult = ScreeningWithHallDto.builder()
                .id(1)
                .price(BigDecimal.valueOf(200, 0))
                .startTime(String.valueOf(LocalDateTime.of(2024, 1, 1, 16, 30))
                        .replace("T", " "))
                .movieId(1)
                .hallId(1)
                .hallName("Hall1")
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
