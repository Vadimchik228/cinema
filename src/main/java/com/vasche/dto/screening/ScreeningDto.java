package com.vasche.dto.screening;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class ScreeningDto {
    private final int id;
    private final String startTime;
    private final BigDecimal price;
    private final int movieId;
    private final int hallId;
}
