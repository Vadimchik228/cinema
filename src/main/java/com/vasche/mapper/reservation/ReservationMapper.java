package com.vasche.mapper.reservation;

import com.vasche.dto.reservation.ReservationDto;
import com.vasche.entity.Reservation;
import com.vasche.mapper.Mapper;

public class ReservationMapper implements Mapper<Reservation, ReservationDto> {
    public ReservationMapper() {
    }

    @Override
    public ReservationDto mapFrom(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .userId(reservation.getUserId())
                .screeningId(reservation.getScreeningId())
                .seatId(reservation.getSeatId())
                .build();
    }
}
