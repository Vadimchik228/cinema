package com.vasche.mapper.line;

import com.vasche.dto.line.CreateLineDto;
import com.vasche.entity.Line;
import com.vasche.mapper.Mapper;

public class CreateLineMapper implements Mapper<CreateLineDto, Line> {

    public CreateLineMapper() {
    }

    @Override
    public Line mapFrom(final CreateLineDto createLineDto) {
        return Line.builder()
                .number(Integer.parseInt(createLineDto.getNumber()))
                .hallId(Integer.parseInt(createLineDto.getHallId()))
                .build();
    }

}
