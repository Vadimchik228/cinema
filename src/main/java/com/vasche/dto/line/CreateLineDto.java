package com.vasche.dto.line;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateLineDto {
    String number;
    String hallId;
}
