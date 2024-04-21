package com.vasche.mapper.hall;

import com.vasche.dto.hall.CreateHallDto;
import com.vasche.entity.Hall;
import com.vasche.mapper.Mapper;

public class CreateHallMapper implements Mapper<CreateHallDto, Hall> {

    public CreateHallMapper() {
    }

    @Override
    public Hall mapFrom(final CreateHallDto createHallDto) {
        return Hall.builder()
                .name(createHallDto.getName())
                .build();
    }

}