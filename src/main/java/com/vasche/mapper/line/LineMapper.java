package com.vasche.mapper.line;

import com.vasche.dto.line.LineDto;
import com.vasche.entity.Line;
import com.vasche.mapper.Mapper;

public class LineMapper implements Mapper<Line, LineDto> {

    public LineMapper() {
    }

    @Override
    public LineDto mapFrom(Line line) {
        return LineDto.builder()
                .id(line.getId())
                .number(line.getNumber())
                .hallId(line.getHallId())
                .build();
    }
}
