package com.vasche.dto.line;

import com.vasche.dto.seat.SeatAllDataDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public final class LineAllDataDto {
    private final int id;
    private final int number;
    private final int hallId;
    private final List<SeatAllDataDto> seats;
}

