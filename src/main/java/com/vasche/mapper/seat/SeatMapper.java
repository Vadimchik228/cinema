package com.vasche.mapper.seat;

import com.vasche.dto.seat.SeatDto;
import com.vasche.entity.Seat;
import com.vasche.mapper.Mapper;

public class SeatMapper implements Mapper<Seat, SeatDto> {

    public SeatMapper() {
    }

    @Override
    public SeatDto mapFrom(final Seat seat) {
        return SeatDto.builder()
                .id(seat.getId())
                .number(seat.getNumber())
                .lineId(seat.getLineId())
                .build();
    }
}