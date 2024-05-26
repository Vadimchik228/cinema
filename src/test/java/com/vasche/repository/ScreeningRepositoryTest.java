package com.vasche.repository;

import com.vasche.entity.*;
import com.vasche.exception.RepositoryException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.vasche.constant.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScreeningRepositoryTest extends RepositoryTestBase {

    private Hall hall;
    private Line line;
    private Seat seat;
    private User user;
    private Movie movie;

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

        line = lineDao.save(getLine(1, hall.getId()));
        seat = seatDao.save(getSeat(1, line.getId()));

        user = userDao.save(getUser(EMAIL1));

        movie = movieDao.save(getMovie(MOVIE_TITLE1));
    }

    @Test
    void save() throws RepositoryException {

        Screening screening = screeningDao.save(getScreening(movie.getId(), hall.getId()));

        Screening actualResult = screeningDao.save(screening);

        assertThat(actualResult).isNotNull();
    }

    @Test
    void findById() throws RepositoryException {

        Screening screening = screeningDao.save(getScreening(movie.getId(), hall.getId()));

        Optional<Screening> actualResult = screeningDao.findById(screening.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(screening);
    }

    @Test
    void findByReservationId() throws RepositoryException {
        Screening screening = screeningDao.save(getScreening(movie.getId(), hall.getId()));
        Reservation reservation = reservationDao.save(getReservation(user.getId(), screening.getId(), seat.getId()));

        Optional<Screening> actualResult = screeningDao.findByReservationId(reservation.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(screening);
    }

    @Test
    void findAll() throws RepositoryException {

        Screening screening1 = screeningDao.save(getScreening(movie.getId(), hall.getId()));
        Screening screening2 = screeningDao.save(getScreening(movie.getId(), hall.getId()));
        Screening screening3 = screeningDao.save(getScreening(movie.getId(), hall.getId()));

        List<Screening> actualResult = screeningDao.findAll();

        assertThat(actualResult).hasSize(3);
        List<Integer> screeningIds = actualResult.stream()
                .map(Screening::getId)
                .toList();
        assertThat(screeningIds).contains(screening1.getId(), screening2.getId(), screening3.getId());

    }

    @Test
    void shouldNotFindByIdIfHallDoesNotExist() throws RepositoryException {
        screeningDao.save(getScreening(movie.getId(), hall.getId()));

        Optional<Screening> actualResult = screeningDao.findById(2000000);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void deleteExistingEntity() throws RepositoryException {

        Screening screening = screeningDao.save(getScreening(movie.getId(), hall.getId()));

        boolean actualResult = screeningDao.delete(screening.getId());

        assertTrue(actualResult);
    }

    @Test
    void deleteNotExistingEntity() throws RepositoryException {
        screeningDao.save(getScreening(movie.getId(), hall.getId()));

        boolean actualResult = screeningDao.delete(100500);
        assertFalse(actualResult);
    }

    @Test
    void update() throws RepositoryException {
        Screening screening = screeningDao.save(getScreening(movie.getId(), hall.getId()));

        screening.setPrice(BigDecimal.valueOf(100000, 2));
        screening.setStartTime(LocalDateTime.of(2025, 1, 1, 12, 30));

        screeningDao.update(screening);

        if (screeningDao.findById(screening.getId()).isPresent()) {
            Screening updatedScreening = screeningDao.findById(screening.getId()).get();
            assertThat(updatedScreening).isEqualTo(screening);
        } else {
            Assertions.fail();
        }
    }


    @Test
    void findAllAvailableByMovieId() throws RepositoryException {
        Seat seat2 = seatDao.save(getSeat(2, line.getId()));
        Seat seat3 = seatDao.save(getSeat(3, line.getId()));

        Screening screening1 = screeningDao.save(getScreening(movie.getId(), hall.getId()));
        Screening screening2 = screeningDao.save(getScreening(movie.getId(), hall.getId()));

        reservationDao.save(getReservation(user.getId(), screening1.getId(),
                seat.getId()));
        reservationDao.save(getReservation(user.getId(), screening1.getId(),
                seat2.getId()));
        reservationDao.save(getReservation(user.getId(), screening1.getId(),
                seat3.getId()));
        reservationDao.save(getReservation(user.getId(), screening2.getId(),
                seat.getId()));

        List<Screening> actualResult = screeningDao.findAllAvailableByMovieId(movie.getId());

        assertThat(actualResult).hasSize(1);
        List<Integer> screeningIds = actualResult.stream()
                .map(Screening::getId)
                .toList();
        assertThat(screeningIds).contains(screening2.getId());
    }
}