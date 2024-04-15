package com.vasche.dto.screening;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class ScreeningDto {
    Integer id;
    LocalDateTime startTime;
    BigDecimal price;
    Integer movieId;
    Integer hallId;
}
