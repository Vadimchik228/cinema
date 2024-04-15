package com.vasche.dto.screening;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class CreateScreeningDto {
    String startTime;
    String price;
    String movieId;
    String hallId;
}
