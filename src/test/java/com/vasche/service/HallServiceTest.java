package com.vasche.service;

import com.vasche.repository.HallRepository;
import com.vasche.dto.hall.CreateHallDto;
import com.vasche.dto.hall.HallDto;
import com.vasche.entity.Hall;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.hall.CreateHallMapper;
import com.vasche.mapper.hall.HallMapper;
import com.vasche.validator.CreateHallValidator;
import com.vasche.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.vasche.constant.TestConstant.HALL_NAME1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HallServiceTest extends ServiceTestBase {

    @Mock
    private CreateHallValidator createHallValidator;

    @Mock
    private HallRepository hallDao;

    @Mock
    private CreateHallMapper createHallMapper;

    @Mock
    private HallMapper hallMapper;

    @InjectMocks
    private HallService hallService;

    @Test
    void testCreateIfValidationPassed() throws RepositoryException {

        CreateHallDto createHallDto = getCreateHallDto();
        Hall hall = getHall();
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        doReturn(validationResult).when(createHallValidator).isValid(createHallDto);

        when(validationResult.isValid())
                .thenReturn(true);
        doReturn(hall).when(createHallMapper).mapFrom(createHallDto);

        doReturn(hall).when(hallDao).save(hall);

        Integer actualResult = hallService.create(createHallDto);

        assertThat(actualResult)
                .isEqualTo(1);
    }

    @Test
    void testCreateIfValidationFailed() {
        CreateHallDto createHallDto = getCreateHallDto();
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        when(createHallValidator.isValid(createHallDto))
                .thenReturn(validationResult);
        when(validationResult.isValid())
                .thenReturn(false);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> hallService.create(createHallDto));
    }

    @Test
    void testUpdateIfValidationFailed() {
        CreateHallDto createHallDto = getCreateHallDto();
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        when(createHallValidator.isValid(createHallDto))
                .thenReturn(validationResult);
        when(validationResult.isValid())
                .thenReturn(false);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> hallService.create(createHallDto));
    }

    @Test
    void testFindByIdIfHallExists() throws RepositoryException {
        Hall hall = getHall();
        HallDto HallDto = getHallDto();
        when(hallDao.findById(hall.getId()))
                .thenReturn(Optional.of(hall));
        when(hallMapper.mapFrom(hall))
                .thenReturn(HallDto);

        final Optional<HallDto> actualResult = hallService.findById(hall.getId());

        assertThat(actualResult)
                .isPresent();
        assertThat(actualResult.get())
                .isEqualTo(HallDto);
    }

    @Test
    void testDelete() throws RepositoryException {
        when(hallDao.delete(1))
                .thenReturn(true);

        boolean actualResult = hallService.delete(1);

        assertThat(actualResult).isTrue();
    }

    @Test
    void testFindByIdIfHallDoesNotExist() throws RepositoryException {
        when(hallDao.findById(1))
                .thenReturn(Optional.empty());

        final Optional<HallDto> actualResult = hallService.findById(1);

        assertThat(actualResult)
                .isEmpty();
        verifyNoInteractions(hallMapper);
    }

    @Test
    void testFindByLineIdIfHallExists() throws RepositoryException {
        Hall hall = getHall();
        HallDto hallDto = getHallDto();
        when(hallDao.findByLineId(1))
                .thenReturn(Optional.of(hall));
        when(hallMapper.mapFrom(hall))
                .thenReturn(hallDto);

        final Optional<HallDto> actualResult = hallService.findByLineId(1);

        assertThat(actualResult)
                .isPresent();
        assertThat(actualResult.get())
                .isEqualTo(hallDto);
    }

    @Test
    void testFindByLineIdIfHallDoesNotExist() throws RepositoryException {
        when(hallDao.findByLineId(1))
                .thenReturn(Optional.empty());

        final Optional<HallDto> actualResult = hallService.findByLineId(1);

        assertThat(actualResult)
                .isEmpty();
        verifyNoInteractions(hallMapper);
    }


    @Test
    void testFindByNameIfHallExists() throws RepositoryException {
        Hall hall = getHall();
        HallDto hallDto = getHallDto();
        when(hallDao.findByName(hall.getName()))
                .thenReturn(Optional.of(hall));
        when(hallMapper.mapFrom(hall))
                .thenReturn(hallDto);

        final Optional<HallDto> actualResult = hallService.findByName(hall.getName());

        assertThat(actualResult)
                .isPresent();
        assertThat(actualResult.get())
                .isEqualTo(hallDto);
    }

    @Test
    void testFindByNameIfHallDoesNotExist() throws RepositoryException {
        when(hallDao.findByName(HALL_NAME1))
                .thenReturn(Optional.empty());

        final Optional<HallDto> actualResult = hallService.findByName(HALL_NAME1);

        assertThat(actualResult)
                .isEmpty();
        verifyNoInteractions(hallMapper);
    }

    @Test
    void testFindAll() throws RepositoryException {

        Hall hall = getHall();
        List<Hall> halls = List.of(hall);
        HallDto hallDto = getHallDto();
        when(hallDao.findAll())
                .thenReturn(halls);
        when(hallMapper.mapFrom(hall))
                .thenReturn(hallDto);

        final List<HallDto> actualResult = hallService.findAll();

        assertThat(actualResult)
                .hasSize(1);
        actualResult.stream().map(HallDto::getId)
                .forEach(id -> assertThat(id).isEqualTo(1));
    }
}