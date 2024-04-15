package com.vasche.dto.reservation;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateReservationDto {
    String userId;
    String screeningId;
    String seatId;
}
