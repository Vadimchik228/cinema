package com.vasche.repository;

import com.vasche.entity.Hall;
import com.vasche.entity.Line;
import com.vasche.entity.Seat;
import com.vasche.exception.RepositoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.vasche.constant.TestConstant.HALL_NAME1;
import static com.vasche.constant.TestConstant.HALL_NAME2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class LineRepositoryTest extends RepositoryTestBase {

    private final HallRepository hallDao = new HallRepository();
    private final LineRepository lineDao = new LineRepository();
    private final SeatRepository seatDao = new SeatRepository();

    @BeforeEach
    void insertHalls() throws RepositoryException {
        Hall hall1 = getHall(HALL_NAME1);
        hallDao.save(hall1);

        Hall hall2 = getHall(HALL_NAME2);
        hallDao.save(hall2);
    }

    @Test
    void save() throws RepositoryException {
        Optional<Hall> hall = hallDao.findByName(HALL_NAME1);

        Line line = getLine(1, hall.get().getId());
        Line actualResult = lineDao.save(line);

        assertNotNull(actualResult.getId());
    }

    @Test
    void findById() throws RepositoryException {
        Optional<Hall> hall = hallDao.findByName(HALL_NAME1);

        Line line = lineDao.save(getLine(1, hall.get().getId()));

        Optional<Line> actualResult = lineDao.findById(line.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(line);
    }

    @Test
    void findAll() throws RepositoryException {

        Optional<Hall> hall1 = hallDao.findByName(HALL_NAME1);
        Optional<Hall> hall2 = hallDao.findByName(HALL_NAME2);

        Line line1 = lineDao.save(getLine(1, hall1.get().getId()));
        Line line2 = lineDao.save(getLine(2, hall2.get().getId()));
        Line line3 = lineDao.save(getLine(1, hall1.get().getId()));

        List<Line> actualResult = lineDao.findAll();

        assertThat(actualResult).hasSize(3);
        List<Integer> moviesIds = actualResult.stream()
                .map(Line::getId)
                .toList();
        assertThat(moviesIds).contains(line1.getId(), line2.getId(), line3.getId());

    }

    @Test
    void findAllByHallId() throws RepositoryException {

        Optional<Hall> hall1 = hallDao.findByName(HALL_NAME1);
        Optional<Hall> hall2 = hallDao.findByName(HALL_NAME2);

        Line line1 = lineDao.save(getLine(1, hall1.get().getId()));
        Line line2 = lineDao.save(getLine(2, hall2.get().getId()));
        Line line3 = lineDao.save(getLine(1, hall1.get().getId()));

        List<Line> actualResult = lineDao.findAllByHallId(hall1.get().getId());

        assertThat(actualResult).hasSize(2);
        List<Integer> moviesIds = actualResult.stream()
                .map(Line::getId)
                .toList();
        assertThat(moviesIds).contains(line1.getId(), line3.getId());

    }

    @Test
    void findBySeatId() throws RepositoryException {

        Optional<Hall> hall = hallDao.findByName(HALL_NAME1);

        Line line = lineDao.save(getLine(1, hall.get().getId()));

        Seat seat = seatDao.save(getSeat(1, line.getId()));

        Optional<Line> actualResult = lineDao.findBySeatId(seat.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(line);

    }


    @Test
    void shouldNotFindByIdIfHallDoesNotExist() throws RepositoryException {
        Optional<Hall> hall = hallDao.findByName(HALL_NAME1);

        Line line = lineDao.save(getLine(1, hall.get().getId()));

        Optional<Line> actualResult = lineDao.findById(2000000);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void deleteExistingEntity() throws RepositoryException {
        Optional<Hall> hall = hallDao.findByName(HALL_NAME1);

        Line line = lineDao.save(getLine(1, hall.get().getId()));

        boolean actualResult = lineDao.delete(line.getId());

        assertTrue(actualResult);
    }

    @Test
    void deleteNotExistingEntity() throws RepositoryException {
        Optional<Hall> hall = hallDao.findByName(HALL_NAME1);
        lineDao.save(getLine(1, hall.get().getId()));

        boolean actualResult = lineDao.delete(100500);
        assertFalse(actualResult);
    }

    @Test
    void update() throws RepositoryException {
        Optional<Hall> hall = hallDao.findByName(HALL_NAME1);

        Line line = lineDao.save(getLine(1, hall.get().getId()));

        line.setNumber(2);

        lineDao.update(line);

        Line updatedLine = lineDao.findById(line.getId()).get();
        assertThat(updatedLine).isEqualTo(line);
    }
}
