package com.vasche.dto.seat;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateSeatDto {
    String number;
    String lineId;
}