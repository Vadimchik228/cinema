package com.vasche.dto.line;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class LineDto {
    private final int id;
    private final int number;
    private final int hallId;
}
