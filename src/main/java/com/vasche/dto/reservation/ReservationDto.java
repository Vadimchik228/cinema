package com.vasche.dto.reservation;

import com.vasche.entity.Genre;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReservationDto {
    Integer id;
    Integer userId;
    Integer screeningId;
    Integer seatId;
}
