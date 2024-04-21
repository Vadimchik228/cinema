package com.vasche.mapper.hall;

import com.vasche.dto.hall.HallDto;
import com.vasche.entity.Hall;
import com.vasche.mapper.Mapper;

public class HallMapper implements Mapper<Hall, HallDto> {

    public HallMapper() {
    }

    @Override
    public HallDto mapFrom(Hall hall) {
        return HallDto.builder()
                .id(hall.getId())
                .name(hall.getName())
                .build();
    }
}
