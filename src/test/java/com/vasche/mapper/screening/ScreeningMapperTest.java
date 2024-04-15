package com.vasche.mapper.screening;

import com.vasche.dto.screening.ScreeningDto;
import com.vasche.entity.Screening;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ScreeningMapperTest {
    private final ScreeningMapper mapper = ScreeningMapper.getInstance();

    @Test
    void map() {
        Screening screening = Screening.builder()
                .id(1)
                .price(BigDecimal.valueOf(200, 0))
                .startTime(LocalDateTime.of(2024, 1, 1, 16, 30))
                .movieId(1)
                .hallId(1)
                .build();

        ScreeningDto actualResult = mapper.mapFrom(screening);

        ScreeningDto expectedResult = ScreeningDto.builder()
                .id(1)
                .price(BigDecimal.valueOf(200, 0))
                .startTime(LocalDateTime.of(2024, 1, 1, 16, 30))
                .movieId(1)
                .hallId(1)
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
