package com.vasche.mapper.reservation;

import com.vasche.dto.reservation.CreateReservationDto;
import com.vasche.entity.Reservation;
import com.vasche.mapper.Mapper;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateReservationMapper implements Mapper<CreateReservationDto, Reservation> {
    private static final CreateReservationMapper INSTANCE = new CreateReservationMapper();

    public static CreateReservationMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Reservation mapFrom(final CreateReservationDto createReservationDto) {
        return Reservation.builder()
                .userId(Integer.valueOf(createReservationDto.getUserId()))
                .screeningId(Integer.valueOf(createReservationDto.getScreeningId()))
                .seatId(Integer.valueOf(createReservationDto.getSeatId()))
                .build();
    }
}
