package com.vasche.dto.reservation;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class CreateReservationDto {
    private final String userId;
    private final String screeningId;
    private final String seatId;
}
