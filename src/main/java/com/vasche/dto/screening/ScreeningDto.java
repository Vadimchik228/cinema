package com.vasche.dto.screening;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class ScreeningDto {
    private final int id;
    private final LocalDateTime startTime;
    private final BigDecimal price;
    private final int movieId;
    private final int hallId;
}
