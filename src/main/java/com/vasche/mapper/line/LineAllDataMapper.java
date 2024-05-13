package com.vasche.mapper.line;

import com.vasche.dto.line.LineAllDataDto;
import com.vasche.dto.seat.SeatAllDataDto;
import com.vasche.entity.Line;
import com.vasche.mapper.Mapper;

import java.util.List;

public class LineAllDataMapper implements Mapper<Line, LineAllDataDto> {
    public LineAllDataMapper() {
    }

    @Override
    public LineAllDataDto mapFrom(Line line) {
        return null;
    }

    public LineAllDataDto mapFrom(Line line, List<SeatAllDataDto> seats) {
        return LineAllDataDto.builder()
                .id(line.getId())
                .number(line.getNumber())
                .hallId(line.getHallId())
                .seats(seats)
                .build();
    }
}
