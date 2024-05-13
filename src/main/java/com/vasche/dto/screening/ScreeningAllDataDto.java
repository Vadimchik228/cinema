package com.vasche.dto.screening;

import com.vasche.entity.Genre;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class ScreeningAllDataDto {
    private final int id;
    private final String startTime;
    private final BigDecimal price;
    private final String title;
    private final String description;
    private final int durationMin;
    private final int minimumAge;
    private final String imageUrl;
    private final Genre genre;
    private final String hallName;
}
