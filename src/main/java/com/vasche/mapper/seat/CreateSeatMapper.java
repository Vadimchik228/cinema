package com.vasche.mapper.seat;

import com.vasche.dto.seat.CreateSeatDto;
import com.vasche.entity.Seat;
import com.vasche.mapper.Mapper;

public class CreateSeatMapper implements Mapper<CreateSeatDto, Seat> {
    public CreateSeatMapper() {
    }

    @Override
    public Seat mapFrom(final CreateSeatDto createSeatDto) {
        return Seat.builder()
                .number(Integer.parseInt(createSeatDto.getNumber()))
                .lineId(Integer.parseInt(createSeatDto.getLineId()))
                .build();
    }
}