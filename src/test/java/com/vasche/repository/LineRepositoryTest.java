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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LineRepositoryTest extends RepositoryTestBase {

    private final HallRepository hallRepository = new HallRepository();
    private final LineRepository lineRepository = new LineRepository();
    private final SeatRepository seatRepository = new SeatRepository();

    @BeforeEach
    void insertHalls() throws RepositoryException {
        Hall hall1 = getHall(HALL_NAME1);
        hallRepository.save(hall1);

        Hall hall2 = getHall(HALL_NAME2);
        hallRepository.save(hall2);
    }

    @Test
    void save() throws RepositoryException {
        Optional<Hall> hall = hallRepository.findByName(HALL_NAME1);

        Line line = getLine(1, hall.get().getId());
        Line actualResult = lineRepository.save(line);

        assertThat(actualResult).isNotNull();
    }

    @Test
    void findById() throws RepositoryException {
        Optional<Hall> hall = hallRepository.findByName(HALL_NAME1);

        Line line = lineRepository.save(getLine(1, hall.get().getId()));

        Optional<Line> actualResult = lineRepository.findById(line.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(line);
    }

    @Test
    void findAll() throws RepositoryException {

        Optional<Hall> hall1 = hallRepository.findByName(HALL_NAME1);
        Optional<Hall> hall2 = hallRepository.findByName(HALL_NAME2);

        Line line1 = lineRepository.save(getLine(1, hall1.get().getId()));
        Line line2 = lineRepository.save(getLine(2, hall2.get().getId()));
        Line line3 = lineRepository.save(getLine(1, hall1.get().getId()));

        List<Line> actualResult = lineRepository.findAll();

        assertThat(actualResult).hasSize(3);
        List<Integer> moviesIds = actualResult.stream()
                .map(Line::getId)
                .toList();
        assertThat(moviesIds).contains(line1.getId(), line2.getId(), line3.getId());

    }

    @Test
    void findAllByHallId() throws RepositoryException {

        Optional<Hall> hall1 = hallRepository.findByName(HALL_NAME1);
        Optional<Hall> hall2 = hallRepository.findByName(HALL_NAME2);

        Line line1 = lineRepository.save(getLine(1, hall1.get().getId()));
        Line line2 = lineRepository.save(getLine(2, hall2.get().getId()));
        Line line3 = lineRepository.save(getLine(1, hall1.get().getId()));

        List<Line> actualResult = lineRepository.findAllByHallId(hall1.get().getId());

        assertThat(actualResult).hasSize(2);
        List<Integer> moviesIds = actualResult.stream()
                .map(Line::getId)
                .toList();
        assertThat(moviesIds).contains(line1.getId(), line3.getId());

    }

    @Test
    void findBySeatId() throws RepositoryException {

        Optional<Hall> hall = hallRepository.findByName(HALL_NAME1);

        Line line = lineRepository.save(getLine(1, hall.get().getId()));

        Seat seat = seatRepository.save(getSeat(1, line.getId()));

        Optional<Line> actualResult = lineRepository.findBySeatId(seat.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(line);

    }


    @Test
    void shouldNotFindByIdIfHallDoesNotExist() throws RepositoryException {

        Optional<Line> actualResult = lineRepository.findById(2000000);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void deleteExistingEntity() throws RepositoryException {
        Optional<Hall> hall = hallRepository.findByName(HALL_NAME1);

        Line line = lineRepository.save(getLine(1, hall.get().getId()));

        boolean actualResult = lineRepository.delete(line.getId());

        assertTrue(actualResult);
    }

    @Test
    void deleteNotExistingEntity() throws RepositoryException {
        Optional<Hall> hall = hallRepository.findByName(HALL_NAME1);
        lineRepository.save(getLine(1, hall.get().getId()));

        boolean actualResult = lineRepository.delete(100500);
        assertFalse(actualResult);
    }

    @Test
    void update() throws RepositoryException {
        Optional<Hall> hall = hallRepository.findByName(HALL_NAME1);

        Line line = lineRepository.save(getLine(1, hall.get().getId()));

        line.setNumber(2);

        lineRepository.update(line);

        Line updatedLine = lineRepository.findById(line.getId()).get();
        assertThat(updatedLine).isEqualTo(line);
    }
}
