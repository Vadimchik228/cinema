package com.vasche.dto.seat;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SeatDto {
    Integer id;
    Integer number;
    Integer lineId;
}
