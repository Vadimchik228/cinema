package com.vasche.mapper.seat;

import com.vasche.dto.seat.CreateSeatDto;
import com.vasche.entity.Seat;
import com.vasche.mapper.Mapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateSeatMapper implements Mapper<CreateSeatDto, Seat> {
    private static final CreateSeatMapper INSTANCE = new CreateSeatMapper();

    public static CreateSeatMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Seat mapFrom(final CreateSeatDto createSeatDto) {
        return Seat.builder()
                .number(Integer.valueOf(createSeatDto.getNumber()))
                .lineId(Integer.valueOf(createSeatDto.getLineId()))
                .build();
    }
}