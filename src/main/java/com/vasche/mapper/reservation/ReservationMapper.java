package com.vasche.mapper.reservation;

import com.vasche.dto.reservation.ReservationDto;
import com.vasche.entity.Reservation;
import com.vasche.mapper.Mapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationMapper implements Mapper<Reservation, ReservationDto> {
    private static final ReservationMapper INSTANCE = new ReservationMapper();

    public static ReservationMapper getInstance() {
        return INSTANCE;
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
