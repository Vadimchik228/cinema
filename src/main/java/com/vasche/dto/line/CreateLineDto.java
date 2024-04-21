package com.vasche.dto.line;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class CreateLineDto {
    private final String number;
    private final String hallId;
}
