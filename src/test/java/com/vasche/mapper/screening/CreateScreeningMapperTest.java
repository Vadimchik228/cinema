package com.vasche.mapper.screening;

import com.vasche.dto.screening.CreateScreeningDto;
import com.vasche.entity.Screening;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CreateScreeningMapperTest {
    private final CreateScreeningMapper mapper = new CreateScreeningMapper();

    @Test
    void map() {
        CreateScreeningDto dto = CreateScreeningDto.builder()
                .price("200")
                .startTime("2024-01-01 16:30")
                .hallId("1")
                .movieId("1")
                .build();

        Screening actualResult = mapper.mapFrom(dto);

        Screening expectedResult = Screening.builder()
                .price(BigDecimal.valueOf(200))
                .startTime(LocalDateTime.of(2024, 1, 1, 16, 30))
                .hallId(1)
                .movieId(1)
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
