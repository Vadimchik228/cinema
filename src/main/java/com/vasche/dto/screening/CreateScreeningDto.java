package com.vasche.dto.screening;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class CreateScreeningDto {
    private final String startTime;
    private final String price;
    private final String movieId;
    private final String hallId;
}
