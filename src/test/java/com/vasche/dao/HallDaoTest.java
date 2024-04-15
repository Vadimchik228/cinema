package com.vasche.dao;

import com.vasche.entity.Hall;
import com.vasche.entity.Line;
import com.vasche.exception.DaoException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.vasche.constant.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class HallDaoTest extends DaoTestBase {
    private final HallDao hallDao = HallDao.getInstance();
    private final LineDao lineDao = LineDao.getInstance();

    @Test
    void save() throws DaoException {
        Hall hall = getHall(HALL_NAME1);
        Hall actualResult = hallDao.save(hall);

        assertNotNull(actualResult.getId());
    }

    @Test
    void findById() throws DaoException {
        Hall hall = hallDao.save(getHall(HALL_NAME1));
        Optional<Hall> actualResult = hallDao.findById(hall.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(hall);
    }

    @Test
    void findAll() throws DaoException {

        Hall hall1 = hallDao.save(getHall(HALL_NAME1));
        Hall hall2 = hallDao.save(getHall(HALL_NAME2));
        Hall hall3 = hallDao.save(getHall(HALL_NAME3));

        List<Hall> actualResult = hallDao.findAll();

        assertThat(actualResult).hasSize(3);
        List<Integer> moviesIds = actualResult.stream()
                .map(Hall::getId)
                .toList();
        assertThat(moviesIds).contains(hall1.getId(), hall2.getId(), hall3.getId());

    }

    @Test
    void findByName() throws DaoException {

        Hall hall1 = hallDao.save(getHall(HALL_NAME1));
        Hall hall2 = hallDao.save(getHall(HALL_NAME2));
        Hall hall3 = hallDao.save(getHall(HALL_NAME3));

        Optional<Hall> actualResult = hallDao.findByName(hall2.getName());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(hall2);

    }

    @Test
    void findByLineId() throws DaoException {

        Hall hall = hallDao.save(getHall(HALL_NAME1));

        Line line = lineDao.save(getLine(1, hall.getId()));

        Optional<Hall> actualResult = hallDao.findByLineId(line.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(hall);

    }

    @Test
    void shouldNotFindByIdIfHallDoesNotExist() throws DaoException {
        Hall hall = hallDao.save(getHall(HALL_NAME1));
        Optional<Hall> actualResult = hallDao.findById(2000000);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void deleteExistingEntity() throws DaoException {
        Hall hall = hallDao.save(getHall(HALL_NAME1));
        boolean actualResult = hallDao.delete(hall.getId());

        assertTrue(actualResult);
    }

    @Test
    void deleteNotExistingEntity() throws DaoException {
        hallDao.save(getHall(HALL_NAME1));
        boolean actualResult = hallDao.delete(100500);
        assertFalse(actualResult);
    }

    @Test
    void update() throws DaoException {
        Hall hall = getHall(HALL_NAME1);
        hallDao.save(hall);

        hall.setName(HALL_NAME2);

        hallDao.update(hall);

        Hall updatedHall = hallDao.findById(hall.getId()).get();
        assertThat(updatedHall).isEqualTo(hall);
    }
}
