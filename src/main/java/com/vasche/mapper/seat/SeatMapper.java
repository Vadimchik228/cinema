package com.vasche.mapper.seat;

import com.vasche.dto.seat.SeatDto;
import com.vasche.entity.Seat;
import com.vasche.mapper.Mapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SeatMapper implements Mapper<Seat, SeatDto> {
    private static final SeatMapper INSTANCE = new SeatMapper();

    public static SeatMapper getInstance() {
        return INSTANCE;
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