package com.vasche.mapper.reservation;

import com.vasche.dto.reservation.CreateReservationDto;
import com.vasche.entity.Reservation;
import com.vasche.mapper.Mapper;

public class CreateReservationMapper implements Mapper<CreateReservationDto, Reservation> {

    public CreateReservationMapper() {
    }

    @Override
    public Reservation mapFrom(final CreateReservationDto createReservationDto) {
        return Reservation.builder()
                .userId(Integer.parseInt(createReservationDto.getUserId()))
                .screeningId(Integer.parseInt(createReservationDto.getScreeningId()))
                .seatId(Integer.parseInt(createReservationDto.getSeatId()))
                .build();
    }
}
