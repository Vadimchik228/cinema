package com.vasche.repository;

import com.vasche.entity.Hall;
import com.vasche.entity.Line;
import com.vasche.exception.RepositoryException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.vasche.constant.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HallRepositoryTest extends RepositoryTestBase {
    private final HallRepository hallRepository = new HallRepository();
    private final LineRepository lineRepository = new LineRepository();

    @Test
    void save() throws RepositoryException {
        Hall hall = getHall(HALL_NAME1);
        Hall actualResult = hallRepository.save(hall);

        assertThat(actualResult).isNotNull();
    }

    @Test
    void findById() throws RepositoryException {
        Hall hall = hallRepository.save(getHall(HALL_NAME1));
        Optional<Hall> actualResult = hallRepository.findById(hall.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(hall);
    }

    @Test
    void findAll() throws RepositoryException {

        Hall hall1 = hallRepository.save(getHall(HALL_NAME1));
        Hall hall2 = hallRepository.save(getHall(HALL_NAME2));
        Hall hall3 = hallRepository.save(getHall(HALL_NAME3));

        List<Hall> actualResult = hallRepository.findAll();

        assertThat(actualResult).hasSize(3);
        List<Integer> moviesIds = actualResult.stream().map(Hall::getId).toList();
        assertThat(moviesIds).contains(hall1.getId(), hall2.getId(), hall3.getId());

    }

    @Test
    void findByName() throws RepositoryException {
        Hall hall = hallRepository.save(getHall(HALL_NAME2));
        Optional<Hall> actualResult = hallRepository.findByName(hall.getName());
        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(hall);

    }

    @Test
    void findByLineId() throws RepositoryException {

        Hall hall = hallRepository.save(getHall(HALL_NAME1));

        Line line = lineRepository.save(getLine(1, hall.getId()));

        Optional<Hall> actualResult = hallRepository.findByLineId(line.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(hall);

    }

    @Test
    void shouldNotFindByIdIfHallDoesNotExist() throws RepositoryException {
        Optional<Hall> actualResult = hallRepository.findById(2000000);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void deleteExistingEntity() throws RepositoryException {
        Hall hall = hallRepository.save(getHall(HALL_NAME1));
        boolean actualResult = hallRepository.delete(hall.getId());

        assertTrue(actualResult);
    }

    @Test
    void deleteNotExistingEntity() throws RepositoryException {
        hallRepository.save(getHall(HALL_NAME1));
        boolean actualResult = hallRepository.delete(100500);
        assertFalse(actualResult);
    }

    @Test
    void update() throws RepositoryException {
        Hall hall = getHall(HALL_NAME1);
        hallRepository.save(hall);

        hall.setName(HALL_NAME2);

        hallRepository.update(hall);

        var updatedHall = hallRepository.findById(hall.getId());
        if (updatedHall.isPresent()) {
            assertThat(updatedHall.get()).isEqualTo(hall);
        } else {
            Assertions.fail();
        }
    }
}
