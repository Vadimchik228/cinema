package com.vasche.dto.reservation;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class ReservationDto {
    private final int id;
    private final int userId;
    private final int screeningId;
    private final int seatId;
}
