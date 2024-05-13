package com.vasche.repository;

import com.vasche.entity.*;
import com.vasche.exception.RepositoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.vasche.constant.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationRepositoryTest extends RepositoryTestBase {
    private Seat seat1;
    private Seat seat2;
    private Seat seat3;
    private User user;
    private Screening screening;

    private final HallRepository hallRepository = new HallRepository();
    private final LineRepository lineRepository = new LineRepository();
    private final SeatRepository seatRepository = new SeatRepository();
    private final ReservationRepository reservationRepository = new ReservationRepository();
    private final UserRepository userRepository = new UserRepository();
    private final MovieRepository movieRepository = new MovieRepository();
    private final ScreeningRepository screeningRepository = new ScreeningRepository();

    @BeforeEach
    void insertHallAndLines() throws RepositoryException {
        Hall hall = hallRepository.save(getHall(HALL_NAME1));

        Line line = lineRepository.save(getLine(1, hall.getId()));
        seat1 = seatRepository.save(getSeat(1, line.getId()));
        seat2 = seatRepository.save(getSeat(2, line.getId()));
        seat3 = seatRepository.save(getSeat(3, line.getId()));

        user = userRepository.save(getUser(EMAIL1));

        Movie movie = movieRepository.save(getMovie(MOVIE_TITLE1));
        screening = screeningRepository.save(getScreening(movie.getId(), hall.getId()));
    }

    @Test
    void save() throws RepositoryException {

        Reservation reservation = getReservation(user.getId(), screening.getId(), seat1.getId());

        Reservation actualResult = reservationRepository.save(reservation);

        assertThat(actualResult).isNotNull();
    }

    @Test
    void findById() throws RepositoryException {

        Reservation reservation = reservationRepository.save(getReservation(user.getId(), screening.getId(), seat1.getId()));

        Optional<Reservation> actualResult = reservationRepository.findById(reservation.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(reservation);
    }

    @Test
    void findAll() throws RepositoryException {

        Reservation reservation1 = reservationRepository.save(getReservation(user.getId(), screening.getId(), seat1.getId()));
        Reservation reservation2 = reservationRepository.save(getReservation(user.getId(), screening.getId(), seat2.getId()));
        Reservation reservation3 = reservationRepository.save(getReservation(user.getId(), screening.getId(), seat3.getId()));

        List<Reservation> actualResult = reservationRepository.findAll();

        assertThat(actualResult).hasSize(3);
        List<Integer> reservationIds = actualResult.stream()
                .map(Reservation::getId)
                .toList();
        assertThat(reservationIds).contains(reservation1.getId(), reservation2.getId(), reservation3.getId());

    }

    @Test
    void findAllByUserId() throws RepositoryException {

        Reservation reservation1 = reservationRepository.save(getReservation(user.getId(), screening.getId(), seat1.getId()));
        Reservation reservation2 = reservationRepository.save(getReservation(user.getId(), screening.getId(), seat2.getId()));
        Reservation reservation3 = reservationRepository.save(getReservation(user.getId(), screening.getId(), seat3.getId()));

        List<Reservation> actualResult = reservationRepository.findAllByUserId(user.getId());

        assertThat(actualResult).hasSize(3);
        List<Integer> reservationIds = actualResult.stream()
                .map(Reservation::getId)
                .toList();
        assertThat(reservationIds).contains(reservation1.getId(), reservation2.getId(), reservation3.getId());
    }

    @Test
    void findAllByScreeningId() throws RepositoryException {
        Reservation reservation1 = reservationRepository.save(getReservation(user.getId(), screening.getId(), seat1.getId()));
        Reservation reservation2 = reservationRepository.save(getReservation(user.getId(), screening.getId(), seat2.getId()));
        Reservation reservation3 = reservationRepository.save(getReservation(user.getId(), screening.getId(), seat3.getId()));

        List<Reservation> actualResult = reservationRepository.findAllByScreeningId(screening.getId());

        assertThat(actualResult).hasSize(3);
        List<Integer> reservationIds = actualResult.stream()
                .map(Reservation::getId)
                .toList();
        assertThat(reservationIds).contains(reservation1.getId(), reservation2.getId(), reservation3.getId());
    }


    @Test
    void shouldNotFindByIdIfHallDoesNotExist() throws RepositoryException {
        Optional<Reservation> actualResult = reservationRepository.findById(2000000);
        assertThat(actualResult).isEmpty();
    }

    @Test
    void deleteExistingEntity() throws RepositoryException {

        Reservation reservation = reservationRepository.save(getReservation(user.getId(), screening.getId(), seat1.getId()));

        boolean actualResult = reservationRepository.delete(reservation.getId());

        assertTrue(actualResult);
    }

    @Test
    void deleteNotExistingEntity() throws RepositoryException {
        boolean actualResult = reservationRepository.delete(100500);
        assertFalse(actualResult);
    }
}