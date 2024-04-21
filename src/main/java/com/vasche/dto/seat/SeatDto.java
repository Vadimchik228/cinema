package com.vasche.dto.seat;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class SeatDto {
    private final int id;
    private final int number;
    private final int lineId;
}
