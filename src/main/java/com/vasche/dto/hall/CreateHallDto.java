package com.vasche.dto.hall;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateHallDto {
    String name;
}