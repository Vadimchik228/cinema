package com.vasche.dto.seat;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public class SeatAllDataDto {
    private final int id;
    private final int number;
    private final int lineId;
    private final Boolean isReserved;
}
