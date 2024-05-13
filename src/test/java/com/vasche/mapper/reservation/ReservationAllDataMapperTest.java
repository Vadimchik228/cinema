package com.vasche.mapper.reservation;

import com.vasche.dto.reservation.ReservationAllDataDto;
import com.vasche.entity.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ReservationAllDataMapperTest {
    private final ReservationAllDataMapper mapper = new ReservationAllDataMapper();

    @Test
    void map() {
        Reservation reservation = Reservation.builder()
                .id(1)
                .seatId(1)
                .screeningId(1)
                .userId(1)
                .build();
        Hall hall = Hall.builder()
                .id(1)
                .name("the best hall")
                .build();
        Line line = Line.builder()
                .id(1)
                .hallId(1)
                .number(1)
                .build();
        Seat seat = Seat.builder()
                .id(1)
                .lineId(1)
                .number(1)
                .build();
        Screening screening = Screening.builder()
                .id(1)
                .price(BigDecimal.valueOf(200, 0))
                .startTime(LocalDateTime.of(2024, 1, 1, 16, 30))
                .movieId(1)
                .hallId(1)
                .build();
        User user = User.builder()
                .id(1)
                .firstName("Vadim")
                .lastName("Schebetovskiy")
                .role(Role.CLIENT)
                .email("schebetovskiyvadim@gmail.com")
                .password("lol123!@")
                .build();
        Movie movie = Movie.builder()
                .id(1)
                .title("Home alone")
                .minimumAge(6)
                .description("This is an American film")
                .durationMin(200)
                .genre(Genre.COMEDY)
                .build();

        ReservationAllDataDto actualResult = mapper.mapFrom(reservation, user, screening, movie, hall, line, seat);


        ReservationAllDataDto expectedResult = ReservationAllDataDto.builder()
                .id(1)
                .seatId(1)
                .screeningId(1)
                .userId(1)
                .title("Home alone")
                .minimumAge(6)
                .durationMin(200)
                .firstName("Vadim")
                .lastName("Schebetovskiy")
                .email("schebetovskiyvadim@gmail.com")
                .hallName("the best hall")
                .seatNumber(1)
                .rowNumber(1)
                .startTime(LocalDateTime.of(2024, 1, 1, 16, 30))
                .price(BigDecimal.valueOf(200, 0))
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
