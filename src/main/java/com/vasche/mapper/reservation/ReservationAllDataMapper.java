package com.vasche.mapper.reservation;

import com.vasche.dto.reservation.ReservationAllDataDto;
import com.vasche.entity.*;
import com.vasche.mapper.Mapper;

public class ReservationAllDataMapper implements Mapper<Reservation, ReservationAllDataDto> {

    public ReservationAllDataMapper() {
    }

    @Override
    public ReservationAllDataDto mapFrom(Reservation reservation) {
        return null;
    }

    public ReservationAllDataDto mapFrom(Reservation reservation, User user,
                                         Screening screening, Movie movie,
                                         Hall hall, Line line, Seat seat) {
        return ReservationAllDataDto.builder()
                .id(reservation.getId())
                .userId(reservation.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .screeningId(reservation.getScreeningId())
                .price(screening.getPrice())
                .startTime(screening.getStartTime())
                .title(movie.getTitle())
                .durationMin(movie.getDurationMin())
                .minimumAge(movie.getMinimumAge())
                .hallName(hall.getName())
                .seatId(seat.getId())
                .rowNumber(line.getNumber())
                .seatNumber(seat.getNumber())
                .build();
    }
}

