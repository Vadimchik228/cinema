package com.vasche.repository;

import com.vasche.entity.*;
import com.vasche.exception.RepositoryException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.vasche.constant.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SeatRepositoryTest extends RepositoryTestBase {
    private Hall hall;
    private Line line1;
    private Line line2;
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
        hall = hallRepository.save(getHall(HALL_NAME1));

        line1 = lineRepository.save(getLine(1, hall.getId()));
        line2 = lineRepository.save(getLine(2, hall.getId()));

        user = userRepository.save(getUser(EMAIL1));

        Movie movie = movieRepository.save(getMovie(MOVIE_TITLE1));
        screening = screeningRepository.save(getScreening(movie.getId(), hall.getId()));
    }

    @Test
    void save() throws RepositoryException {

        Seat seat = getSeat(1, line1.getId());

        Seat actualResult = seatRepository.save(seat);

        assertThat(actualResult).isNotNull();
    }

    @Test
    void findById() throws RepositoryException {

        Seat seat = seatRepository.save(getSeat(1, line1.getId()));

        Optional<Seat> actualResult = seatRepository.findById(seat.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(seat);
    }

    @Test
    void findAll() throws RepositoryException {

        Seat seat1 = seatRepository.save(getSeat(1, line1.getId()));
        Seat seat2 = seatRepository.save(getSeat(2, line1.getId()));
        Seat seat3 = seatRepository.save(getSeat(3, line2.getId()));
        Seat seat4 = seatRepository.save(getSeat(4, line2.getId()));

        List<Seat> actualResult = seatRepository.findAll();

        assertThat(actualResult).hasSize(4);
        List<Integer> moviesIds = actualResult.stream()
                .map(Seat::getId)
                .toList();
        assertThat(moviesIds).contains(seat1.getId(), seat2.getId(), seat3.getId(), seat4.getId());

    }

    @Test
    void findAllByLineId() throws RepositoryException {

        Seat seat1 = seatRepository.save(getSeat(1, line1.getId()));
        Seat seat2 = seatRepository.save(getSeat(2, line1.getId()));

        List<Seat> actualResult = seatRepository.findAllByLineId(line1.getId());

        assertThat(actualResult).hasSize(2);
        List<Integer> moviesIds = actualResult.stream()
                .map(Seat::getId)
                .toList();
        assertThat(moviesIds).contains(seat1.getId(), seat2.getId());
    }

    @Test
    void findAllByHallId() throws RepositoryException {

        Seat seat1 = seatRepository.save(getSeat(1, line1.getId()));
        Seat seat2 = seatRepository.save(getSeat(2, line1.getId()));
        Seat seat3 = seatRepository.save(getSeat(3, line2.getId()));
        Seat seat4 = seatRepository.save(getSeat(4, line2.getId()));

        List<Seat> actualResult = seatRepository.findAllByHallId(hall.getId());

        assertThat(actualResult).hasSize(4);
        List<Integer> moviesIds = actualResult.stream()
                .map(Seat::getId)
                .toList();
        assertThat(moviesIds).contains(seat1.getId(), seat2.getId(), seat3.getId(), seat4.getId());
    }


    @Test
    void shouldNotFindByIdIfHallDoesNotExist() throws RepositoryException {
        Optional<Seat> actualResult = seatRepository.findById(2000000);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void deleteExistingEntity() throws RepositoryException {

        Seat seat = seatRepository.save(getSeat(1, line1.getId()));

        boolean actualResult = seatRepository.delete(seat.getId());

        assertTrue(actualResult);
    }

    @Test
    void deleteNotExistingEntity() throws RepositoryException {
        boolean actualResult = seatRepository.delete(100500);
        assertFalse(actualResult);
    }

    @Test
    void update() throws RepositoryException {
        Seat seat = seatRepository.save(getSeat(1, line1.getId()));

        seat.setNumber(2);

        seatRepository.update(seat);

        if (seatRepository.findById(seat.getId()).isPresent()) {
            Seat updatedSeat = seatRepository.findById(seat.getId()).get();
            assertThat(updatedSeat).isEqualTo(seat);
        } else {
            Assertions.fail();
        }
    }

    @Test
    void findByReservationId() throws RepositoryException {
        Seat seat = seatRepository.save(getSeat(1, line1.getId()));
        Reservation reservation = reservationRepository.save(getReservation(user.getId(), screening.getId(), seat.getId()));

        Optional<Seat> actualResult = seatRepository.findByReservationId(reservation.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(seat);
    }

    @Test
    void findAllAvailableSeats() throws RepositoryException {
        Seat seat1 = seatRepository.save(getSeat(1, line1.getId()));
        Seat seat2 = seatRepository.save(getSeat(2, line1.getId()));
        Seat seat3 = seatRepository.save(getSeat(3, line2.getId()));
        seatRepository.save(getSeat(4, line2.getId()));
        reservationRepository.save(getReservation(user.getId(), screening.getId(),
                seat1.getId()));
        reservationRepository.save(getReservation(user.getId(), screening.getId(),
                seat3.getId()));

        List<Seat> actualResult = seatRepository.findAllAvailableByLineId(line1.getId(), screening.getId());

        assertThat(actualResult).hasSize(1);
        List<Integer> seatsIds = actualResult.stream()
                .map(Seat::getId)
                .toList();
        assertThat(seatsIds).contains(seat2.getId());
    }

}