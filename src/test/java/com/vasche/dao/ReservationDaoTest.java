package com.vasche.dao;

import com.vasche.entity.*;
import com.vasche.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.vasche.constant.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationDaoTest extends DaoTestBase {
    private Seat seat1;
    private Seat seat2;
    private Seat seat3;
    private User user;
    private Screening screening;

    private final HallDao hallDao = HallDao.getInstance();
    private final LineDao lineDao = LineDao.getInstance();
    private final SeatDao seatDao = SeatDao.getInstance();
    private final ReservationDao reservationDao = ReservationDao.getInstance();
    private final UserDao userDao = UserDao.getInstance();
    private final MovieDao movieDao = MovieDao.getInstance();
    private final ScreeningDao screeningDao = ScreeningDao.getInstance();

    @BeforeEach
    void insertHallAndLines() throws DaoException {
        Hall hall = hallDao.save(getHall(HALL_NAME1));

        Line line = lineDao.save(getLine(1, hall.getId()));
        seat1 = seatDao.save(getSeat(1, line.getId()));
        seat2 = seatDao.save(getSeat(2, line.getId()));
        seat3 = seatDao.save(getSeat(3, line.getId()));

        user = userDao.save(getUser(EMAIL1));

        Movie movie = movieDao.save(getMovie(MOVIE_TITLE1));
        screening = screeningDao.save(getScreening(movie.getId(), hall.getId()));
    }

    @Test
    void save() throws DaoException {

        Reservation reservation = getReservation(user.getId(), screening.getId(), seat1.getId());

        Reservation actualResult = reservationDao.save(reservation);

        assertNotNull(actualResult.getId());
    }

    @Test
    void findById() throws DaoException {

        Reservation reservation = reservationDao.save(getReservation(user.getId(), screening.getId(), seat1.getId()));

        Optional<Reservation> actualResult = reservationDao.findById(reservation.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(reservation);
    }

    @Test
    void findAll() throws DaoException {

        Reservation reservation1 = reservationDao.save(getReservation(user.getId(), screening.getId(), seat1.getId()));
        Reservation reservation2 = reservationDao.save(getReservation(user.getId(), screening.getId(), seat2.getId()));
        Reservation reservation3 = reservationDao.save(getReservation(user.getId(), screening.getId(), seat3.getId()));

        List<Reservation> actualResult = reservationDao.findAll();

        assertThat(actualResult).hasSize(3);
        List<Integer> reservationIds = actualResult.stream()
                .map(Reservation::getId)
                .toList();
        assertThat(reservationIds).contains(reservation1.getId(), reservation2.getId(), reservation3.getId());

    }

    @Test
    void findAllByUserId() throws DaoException {

        Reservation reservation1 = reservationDao.save(getReservation(user.getId(), screening.getId(), seat1.getId()));
        Reservation reservation2 = reservationDao.save(getReservation(user.getId(), screening.getId(), seat2.getId()));
        Reservation reservation3 = reservationDao.save(getReservation(user.getId(), screening.getId(), seat3.getId()));

        List<Reservation> actualResult = reservationDao.findAllByUserId(user.getId());

        assertThat(actualResult).hasSize(3);
        List<Integer> reservationIds = actualResult.stream()
                .map(Reservation::getId)
                .toList();
        assertThat(reservationIds).contains(reservation1.getId(), reservation2.getId(), reservation3.getId());
    }

    @Test
    void findAllByScreeningId() throws DaoException {
        Reservation reservation1 = reservationDao.save(getReservation(user.getId(), screening.getId(), seat1.getId()));
        Reservation reservation2 = reservationDao.save(getReservation(user.getId(), screening.getId(), seat2.getId()));
        Reservation reservation3 = reservationDao.save(getReservation(user.getId(), screening.getId(), seat3.getId()));

        List<Reservation> actualResult = reservationDao.findAllByScreeningId(screening.getId());

        assertThat(actualResult).hasSize(3);
        List<Integer> reservationIds = actualResult.stream()
                .map(Reservation::getId)
                .toList();
        assertThat(reservationIds).contains(reservation1.getId(), reservation2.getId(), reservation3.getId());
    }


    @Test
    void shouldNotFindByIdIfHallDoesNotExist() throws DaoException {
        Reservation reservation = reservationDao.save(getReservation(user.getId(), screening.getId(), seat1.getId()));

        Optional<Reservation> actualResult = reservationDao.findById(2000000);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void deleteExistingEntity() throws DaoException {

        Reservation reservation = reservationDao.save(getReservation(user.getId(), screening.getId(), seat1.getId()));

        boolean actualResult = reservationDao.delete(reservation.getId());

        assertTrue(actualResult);
    }

    @Test
    void deleteNotExistingEntity() throws DaoException {
        Reservation reservation = reservationDao.save(getReservation(user.getId(), screening.getId(), seat1.getId()));

        boolean actualResult = reservationDao.delete(100500);
        assertFalse(actualResult);
    }
}
