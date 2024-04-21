package com.vasche.dto.seat;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class CreateSeatDto {
    private final String number;
    private final String lineId;
}