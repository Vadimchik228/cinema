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

public class UserRepositoryTest extends RepositoryTestBase {
    private Seat seat1;
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
        Hall hall = hallDao.save(getHall(HALL_NAME1));

        Line line = lineDao.save(getLine(1, hall.getId()));

        Movie movie = movieDao.save(getMovie(MOVIE_TITLE1));
        screening = screeningDao.save(getScreening(movie.getId(), hall.getId()));

        seat1 = seatDao.save(getSeat(1, line.getId()));
    }

    @Test
    void save() throws RepositoryException {

        User user = getUser(EMAIL1);

        User actualResult = userDao.save(user);

        assertThat(actualResult).isNotNull();
    }

    @Test
    void findById() throws RepositoryException {

        User user = userDao.save(getUser(EMAIL1));

        Optional<User> actualResult = userDao.findById(user.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(user);
    }

    @Test
    void findAll() throws RepositoryException {

        User user1 = userDao.save(getUser(EMAIL1));
        User user2 = userDao.save(getUser(EMAIL2));
        User user3 = userDao.save(getUser(EMAIL3));

        List<User> actualResult = userDao.findAll();

        assertThat(actualResult).hasSize(3);
        List<Integer> moviesIds = actualResult.stream()
                .map(User::getId)
                .toList();
        assertThat(moviesIds).contains(user1.getId(), user2.getId(), user3.getId());

    }

    @Test
    void findByEmail() throws RepositoryException {

        User user = userDao.save(getUser(EMAIL1));

        Optional<User> actualResult = userDao.findByEmail(user.getEmail());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(user);
    }

    @Test
    void findByEmailAndPassword() throws RepositoryException {

        User user = userDao.save(getUser(EMAIL1));

        Optional<User> actualResult = userDao.findByEmail(user.getEmail());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(user);
    }

    @Test
    void findByReservationId() throws RepositoryException {

        User user = userDao.save(getUser(EMAIL1));
        Reservation reservation = reservationDao.save(getReservation(user.getId(), screening.getId(), seat1.getId()));

        Optional<User> actualResult = userDao.findByReservationId(reservation.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(user);
    }

    @Test
    void shouldNotFindByIdIfHallDoesNotExist() throws RepositoryException {

        Optional<User> actualResult = userDao.findById(2000000);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void deleteExistingEntity() throws RepositoryException {

        User user = userDao.save(getUser(EMAIL1));

        boolean actualResult = userDao.delete(user.getId());

        assertTrue(actualResult);
    }

    @Test
    void deleteNotExistingEntity() throws RepositoryException {

        boolean actualResult = userDao.delete(100500);
        assertFalse(actualResult);
    }

    @Test
    void update() throws RepositoryException {
        User user = userDao.save(getUser(EMAIL1));

        user.setFirstName("Newname");
        user.setLastName("Newlastname");
        user.setRole(Role.ADMIN);

        userDao.update(user);

        if (userDao.findById(user.getId()).isPresent()) {
            User updatedUser = userDao.findById(user.getId()).get();
            assertThat(updatedUser).isEqualTo(user);
        } else {
            Assertions.fail();
        }
    }
}