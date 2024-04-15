package com.vasche.mapper.line;

import com.vasche.dto.line.LineDto;
import com.vasche.entity.Line;
import com.vasche.mapper.Mapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LineMapper implements Mapper<Line, LineDto> {
    private static final LineMapper INSTANCE = new LineMapper();

    public static LineMapper getInstance() {
        return INSTANCE;
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
