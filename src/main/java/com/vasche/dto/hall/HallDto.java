package com.vasche.dto.hall;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class HallDto {
    private final int id;
    private final String name;
}
