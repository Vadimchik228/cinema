package com.vasche.service;

import com.vasche.dto.filter.MovieFilterDto;
import com.vasche.dto.filter.ScreeningFilterDto;
import com.vasche.dto.hall.CreateHallDto;
import com.vasche.dto.hall.HallDto;
import com.vasche.dto.line.CreateLineDto;
import com.vasche.dto.line.LineDto;
import com.vasche.dto.movie.CreateMovieDto;
import com.vasche.dto.movie.MovieDto;
import com.vasche.dto.reservation.CreateReservationDto;
import com.vasche.dto.reservation.ReservationDto;
import com.vasche.dto.screening.CreateScreeningDto;
import com.vasche.dto.screening.ScreeningDto;
import com.vasche.dto.seat.CreateSeatDto;
import com.vasche.dto.seat.SeatDto;
import com.vasche.dto.user.CreateUserDto;
import com.vasche.dto.user.UserDto;
import com.vasche.entity.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.vasche.constant.TestConstant.HALL_NAME1;
import static com.vasche.constant.TestConstant.MOVIE_TITLE1;

public class ServiceTestBase {

    public static HallDto getHallDto() {
        return HallDto.builder()
                .id(1)
                .name(HALL_NAME1)
                .build();
    }


    public static Hall getHall() {
        return Hall.builder()
                .id(1)
                .name(HALL_NAME1)
                .build();
    }

    public static CreateHallDto getCreateHallDto() {
        return CreateHallDto.builder()
                .name(HALL_NAME1)
                .build();
    }

    public static LineDto getLineDto() {
        return LineDto.builder()
                .id(1)
                .hallId(1)
                .number(1)
                .build();
    }


    public static Line getLine() {
        return Line.builder()
                .id(1)
                .hallId(1)
                .number(1)
                .build();
    }

    public static CreateLineDto getCreateLineDto() {
        return CreateLineDto.builder()
                .hallId("1")
                .number("1")
                .build();
    }

    public MovieFilterDto getMovieFilterDto() {
        return MovieFilterDto.builder()
                .title("1 +")
                .genre(Genre.COMEDY.name())
                .minimumAge("6")
                .build();
    }

    public CreateMovieDto getCreateMovieDto() {
        return CreateMovieDto.builder()
                .title(MOVIE_TITLE1)
                .minimumAge("6")
                .description("This is an American film")
                .durationMin("200")
                .genre(Genre.COMEDY.name())
                .build();
    }

    public MovieDto getMovieDto(Integer movieId) {
        return MovieDto.builder()
                .id(movieId)
                .title(MOVIE_TITLE1)
                .minimumAge(6)
                .description("This is an American film")
                .durationMin(200)
                .genre(Genre.COMEDY)
                .build();
    }

    public Movie getMovie(Integer movieId) {
        return Movie.builder()
                .id(movieId)
                .title(MOVIE_TITLE1)
                .minimumAge(6)
                .description("This is an American film")
                .durationMin(200)
                .genre(Genre.COMEDY)
                .build();
    }

    public static ReservationDto getReservationDto() {
        return ReservationDto.builder()
                .id(1)
                .seatId(1)
                .userId(1)
                .screeningId(1)
                .build();
    }


    public static Reservation getReservation() {
        return Reservation.builder()
                .id(1)
                .seatId(1)
                .userId(1)
                .screeningId(1)
                .build();
    }

    public static CreateReservationDto getCreateReservationDto() {
        return CreateReservationDto.builder()
                .seatId("1")
                .userId("1")
                .screeningId("1")
                .build();
    }

    public ScreeningFilterDto getScreeningFilterDto() {
        return ScreeningFilterDto.builder()
                .title("1 +")
                .genre(Genre.COMEDY.name())
                .date(LocalDate.of(2024, 1, 1).toString())
                .build();
    }

    public CreateScreeningDto getCreateScreeningDto() {
        return CreateScreeningDto.builder()
                .movieId("1")
                .hallId("1")
                .price(BigDecimal.valueOf(1000, 0).toString())
                .startTime(LocalDateTime.of(2024, 1, 1, 12, 30).toString())
                .build();
    }

    public ScreeningDto getScreeningDto(Integer screeningId) {
        return ScreeningDto.builder()
                .id(screeningId)
                .hallId(1)
                .movieId(1)
                .price(BigDecimal.valueOf(1000, 0))
                .startTime(LocalDateTime.of(2024, 1, 1, 12, 30))
                .build();
    }

    public Screening getScreening(Integer screeningId) {
        return Screening.builder()
                .id(screeningId)
                .hallId(1)
                .movieId(1)
                .price(BigDecimal.valueOf(1000, 0))
                .startTime(LocalDateTime.of(2024, 1, 1, 12, 30))
                .build();
    }

    public static UserDto getUserDto() {
        return UserDto.builder()
                .id(1)
                .firstName("Vadim")
                .lastName("Schebetovskiy")
                .email("schebetovskiy@gmail.com")
                .password("vadim123@!")
                .role(Role.valueOf(Role.CLIENT.name()))
                .build();
    }


    public static User getUser() {
        return User.builder()
                .id(1)
                .password("vadim123@!")
                .email("schebetovskiy@gmail.com")
                .role(Role.CLIENT)
                .lastName("Schebetovskiy")
                .firstName("Vadim")
                .build();
    }

    public static CreateUserDto getCreateUserDto() {
        return CreateUserDto.builder()
                .password("vadim123@!")
                .email("schebetovskiy@gmail.com")
                .role(Role.CLIENT.name())
                .lastName("Schebetovskiy")
                .firstName("Vadim")
                .build();
    }


    public static SeatDto getSeatDto() {
        return SeatDto.builder()
                .id(1)
                .number(1)
                .lineId(1)
                .build();
    }


    public static Seat getSeat() {
        return Seat.builder()
                .id(1)
                .number(1)
                .lineId(1)
                .build();
    }

    public static CreateSeatDto getCreateSeatDto() {
        return CreateSeatDto.builder()
                .lineId("1")
                .number("1")
                .build();
    }

    public static Reservation getReservation(final Integer id) {
        return Reservation.builder()
                .id(id)
                .screeningId(1)
                .seatId(1)
                .userId(1)
                .build();
    }
}
