package com.vasche.dto.hall;

import com.vasche.dto.line.LineAllDataDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class HallAllDataDto {
    private final int id;
    private final String name;
    private final List<LineAllDataDto> lines;
}
