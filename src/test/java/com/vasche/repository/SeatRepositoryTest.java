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

public class SeatRepositoryTest extends RepositoryTestBase {
    private Hall hall;
    private Line line1;
    private Line line2;
    private User user;
    private Screening screening;

    private final HallRepository hallDao = new HallRepository();
    private final LineRepository lineDao = new LineRepository();
    private final SeatRepository seatDao = new SeatRepository();
    private final ReservationRepository reservationDao = new ReservationRepository();
    private final UserRepository userDao = new UserRepository();
    private final MovieRepository movieDao = new MovieRepository();
    private final ScreeningRepository screeningDao = new ScreeningRepository();

    @BeforeEach
    void insertHallAndLines() throws RepositoryException {
        hall = hallDao.save(getHall(HALL_NAME1));

        line1 = lineDao.save(getLine(1, hall.getId()));
        line2 = lineDao.save(getLine(2, hall.getId()));

        user = userDao.save(getUser(EMAIL1));

        Movie movie = movieDao.save(getMovie(MOVIE_TITLE1));
        screening = screeningDao.save(getScreening(movie.getId(), hall.getId()));
    }

    @Test
    void save() throws RepositoryException {

        Seat seat = getSeat(1, line1.getId());

        Seat actualResult = seatDao.save(seat);

        assertNotNull(actualResult.getId());
    }

    @Test
    void findById() throws RepositoryException {

        Seat seat = seatDao.save(getSeat(1, line1.getId()));

        Optional<Seat> actualResult = seatDao.findById(seat.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(seat);
    }

    @Test
    void findAll() throws RepositoryException {

        Seat seat1 = seatDao.save(getSeat(1, line1.getId()));
        Seat seat2 = seatDao.save(getSeat(2, line1.getId()));
        Seat seat3 = seatDao.save(getSeat(3, line2.getId()));
        Seat seat4 = seatDao.save(getSeat(4, line2.getId()));

        List<Seat> actualResult = seatDao.findAll();

        assertThat(actualResult).hasSize(4);
        List<Integer> moviesIds = actualResult.stream()
                .map(Seat::getId)
                .toList();
        assertThat(moviesIds).contains(seat1.getId(), seat2.getId(), seat3.getId(), seat4.getId());

    }

    @Test
    void findAllByLineId() throws RepositoryException {

        Seat seat1 = seatDao.save(getSeat(1, line1.getId()));
        Seat seat2 = seatDao.save(getSeat(2, line1.getId()));
        Seat seat3 = seatDao.save(getSeat(3, line2.getId()));
        Seat seat4 = seatDao.save(getSeat(4, line2.getId()));

        List<Seat> actualResult = seatDao.findAllByLineId(line1.getId());

        assertThat(actualResult).hasSize(2);
        List<Integer> moviesIds = actualResult.stream()
                .map(Seat::getId)
                .toList();
        assertThat(moviesIds).contains(seat1.getId(), seat2.getId());
    }

    @Test
    void findAllByHallId() throws RepositoryException {

        Seat seat1 = seatDao.save(getSeat(1, line1.getId()));
        Seat seat2 = seatDao.save(getSeat(2, line1.getId()));
        Seat seat3 = seatDao.save(getSeat(3, line2.getId()));
        Seat seat4 = seatDao.save(getSeat(4, line2.getId()));

        List<Seat> actualResult = seatDao.findAllByHallId(hall.getId());

        assertThat(actualResult).hasSize(4);
        List<Integer> moviesIds = actualResult.stream()
                .map(Seat::getId)
                .toList();
        assertThat(moviesIds).contains(seat1.getId(), seat2.getId(), seat3.getId(), seat4.getId());
    }


    @Test
    void shouldNotFindByIdIfHallDoesNotExist() throws RepositoryException {
        Seat seat = seatDao.save(getSeat(1, line1.getId()));

        Optional<Seat> actualResult = seatDao.findById(2000000);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void deleteExistingEntity() throws RepositoryException {

        Seat seat = seatDao.save(getSeat(1, line1.getId()));

        boolean actualResult = seatDao.delete(seat.getId());

        assertTrue(actualResult);
    }

    @Test
    void deleteNotExistingEntity() throws RepositoryException {
        Seat seat = seatDao.save(getSeat(1, line1.getId()));

        boolean actualResult = seatDao.delete(100500);
        assertFalse(actualResult);
    }

    @Test
    void update() throws RepositoryException {
        Seat seat = seatDao.save(getSeat(1, line1.getId()));

        seat.setNumber(2);

        seatDao.update(seat);

        Seat updatedSeat = seatDao.findById(seat.getId()).get();
        assertThat(updatedSeat).isEqualTo(seat);
    }

    @Test
    void findByReservationId() throws RepositoryException {
        Seat seat = seatDao.save(getSeat(1, line1.getId()));
        Reservation reservation = reservationDao.save(getReservation(user.getId(), screening.getId(), seat.getId()));

        Optional<Seat> actualResult = seatDao.findByReservationId(reservation.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(seat);
    }

    @Test
    void findAllAvailableSeatsByScreeningIdAndHallId() throws RepositoryException {
        Seat seat1 = seatDao.save(getSeat(1, line1.getId()));
        Seat seat2 = seatDao.save(getSeat(2, line1.getId()));
        Seat seat3 = seatDao.save(getSeat(3, line2.getId()));
        Seat seat4 = seatDao.save(getSeat(4, line2.getId()));
        Reservation reservation1 = reservationDao.save(getReservation(user.getId(), screening.getId(),
                seat1.getId()));
        Reservation reservation2 = reservationDao.save(getReservation(user.getId(), screening.getId(),
                seat3.getId()));

        List<Seat> actualResult = seatDao.findAllAvailable(screening.getId(), hall.getId());

        assertThat(actualResult).hasSize(2);
        List<Integer> moviesIds = actualResult.stream()
                .map(Seat::getId)
                .toList();
        assertThat(moviesIds).contains(seat2.getId(), seat4.getId());
    }

}
