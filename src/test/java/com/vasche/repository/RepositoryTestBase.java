package com.vasche.repository;

import com.vasche.entity.*;
import com.vasche.util.ConnectionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static com.vasche.constant.TestConstant.*;
import static com.vasche.constant.TestConstant.CLEAN_USERS_SQL;

public class RepositoryTestBase {
    @BeforeAll
    static void prepareDatabase() throws SQLException {
        try (var connection = ConnectionManager.get();
             var statement = connection.createStatement()) {
            statement.execute(CLEAN_RESERVATIONS_SQL);
            statement.execute(CLEAN_SCREENINGS_SQL);
            statement.execute(CLEAN_MOVIES_SQL);
            statement.execute(CLEAN_SEATS_SQL);
            statement.execute(CLEAN_LINES_SQL);
            statement.execute(CLEAN_HALLS_SQL);
            statement.execute(CLEAN_USERS_SQL);
        }
    }

    @AfterEach
    void cleanData() throws SQLException {
        try (var connection = ConnectionManager.get();
             var statement = connection.createStatement()) {
            statement.execute(CLEAN_RESERVATIONS_SQL);
            statement.execute(CLEAN_SCREENINGS_SQL);
            statement.execute(CLEAN_MOVIES_SQL);
            statement.execute(CLEAN_SEATS_SQL);
            statement.execute(CLEAN_LINES_SQL);
            statement.execute(CLEAN_HALLS_SQL);
            statement.execute(CLEAN_USERS_SQL);
        }
    }

    Hall getHall(String name) {
        return Hall.builder()
                .id(1)
                .name(name)
                .build();
    }

    Line getLine(Integer number, Integer hallId) {
        return Line.builder()
                .id(1)
                .number(number)
                .hallId(hallId)
                .build();
    }

    Seat getSeat(Integer number, Integer lineId) {
        return Seat.builder()
                .id(1)
                .number(number)
                .lineId(lineId)
                .build();
    }

    Reservation getReservation(Integer userId, Integer screeningId, Integer seatId) {
        return Reservation.builder()
                .id(1)
                .userId(userId)
                .seatId(seatId)
                .screeningId(screeningId)
                .build();
    }

    Movie getMovie(String title) {
        return Movie.builder()
                .id(1)
                .title(title)
                .genre(Genre.COMEDY)
                .minimumAge(12)
                .durationMin(60)
                .description("Some description")
                .build();
    }

    User getUser(String email) {
        return User.builder()
                .id(1)
                .password("Vadim123!@")
                .email(email)
                .firstName("Vadim")
                .lastName("Schebet")
                .role(Role.CLIENT)
                .build();
    }

    Screening getScreening(Integer movieId, Integer hallId) {
        return Screening.builder()
                .id(1)
                .price(BigDecimal.valueOf(1000, 2))
                .startTime(LocalDateTime.of(2024,5,15,7,30))
                .movieId(movieId)
                .hallId(hallId)
                .build();
    }
}
