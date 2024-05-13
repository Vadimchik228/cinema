package com.vasche.mapper.seat;

import com.vasche.dto.seat.SeatAllDataDto;
import com.vasche.entity.Seat;
import com.vasche.mapper.Mapper;

public class SeatAllDataMapper implements Mapper<Seat, SeatAllDataDto> {

    public SeatAllDataMapper() {
    }

    @Override
    public SeatAllDataDto mapFrom(final Seat seat) {
        return null;
    }

    public SeatAllDataDto mapFrom(final Seat seat, final Boolean isReserved) {
        return SeatAllDataDto.builder()
                .id(seat.getId())
                .number(seat.getNumber())
                .lineId(seat.getLineId())
                .isReserved(isReserved)
                .build();
    }
}
