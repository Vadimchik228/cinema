package com.vasche.mapper.hall;

import com.vasche.dto.hall.HallAllDataDto;
import com.vasche.dto.line.LineAllDataDto;
import com.vasche.entity.Hall;
import com.vasche.mapper.Mapper;

import java.util.List;

public class HallAllDataMapper implements Mapper<Hall, HallAllDataDto> {
    public HallAllDataMapper() {
    }

    @Override
    public HallAllDataDto mapFrom(Hall hall) {
        return null;
    }

    public HallAllDataDto mapFrom(final Hall hall, final List<LineAllDataDto> lines) {
        return HallAllDataDto.builder()
                .id(hall.getId())
                .name(hall.getName())
                .lines(lines)
                .build();
    }
}