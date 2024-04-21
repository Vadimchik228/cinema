package com.vasche.service;

import com.vasche.repository.LineRepository;
import com.vasche.dto.line.CreateLineDto;
import com.vasche.dto.line.LineDto;
import com.vasche.entity.Line;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.line.CreateLineMapper;
import com.vasche.mapper.line.LineMapper;
import com.vasche.validator.CreateLineValidator;
import com.vasche.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest extends ServiceTestBase {

    @Mock
    private CreateLineValidator createLineValidator;

    @Mock
    private LineRepository lineDao;

    @Mock
    private CreateLineMapper createLineMapper;

    @Mock
    private LineMapper lineMapper;

    @InjectMocks
    private LineService lineService;

    @Test
    void testCreateIfValidationPassed() throws RepositoryException {

        CreateLineDto createLineDto = getCreateLineDto();
        Line line = getLine();
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        doReturn(validationResult).when(createLineValidator).isValid(createLineDto);

        when(validationResult.isValid())
                .thenReturn(true);
        doReturn(line).when(createLineMapper).mapFrom(createLineDto);

        doReturn(line).when(lineDao).save(line);

        Integer actualResult = lineService.create(createLineDto);

        assertThat(actualResult)
                .isEqualTo(1);
    }

    @Test
    void testCreateIfValidationFailed() {
        CreateLineDto createLineDto = getCreateLineDto();
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        when(createLineValidator.isValid(createLineDto))
                .thenReturn(validationResult);
        when(validationResult.isValid())
                .thenReturn(false);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> lineService.create(createLineDto));
    }

    @Test
    void testUpdateIfValidationFailed() {
        CreateLineDto createLineDto = getCreateLineDto();
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        when(createLineValidator.isValid(createLineDto))
                .thenReturn(validationResult);
        when(validationResult.isValid())
                .thenReturn(false);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> lineService.create(createLineDto));
    }

    @Test
    void testDelete() throws RepositoryException {
        when(lineDao.delete(1))
                .thenReturn(true);

        boolean actualResult = lineService.delete(1);

        assertThat(actualResult).isTrue();
    }

    @Test
    void testFindByIdIfLineExists() throws RepositoryException {
        Line line = getLine();
        LineDto LineDto = getLineDto();
        when(lineDao.findById(line.getId()))
                .thenReturn(Optional.of(line));
        when(lineMapper.mapFrom(line))
                .thenReturn(LineDto);

        final Optional<LineDto> actualResult = lineService.findById(line.getId());

        assertThat(actualResult)
                .isPresent();
        assertThat(actualResult.get())
                .isEqualTo(LineDto);
    }

    @Test
    void testFindByIdIfLineDoesNotExist() throws RepositoryException {
        when(lineDao.findById(1))
                .thenReturn(Optional.empty());

        final Optional<LineDto> actualResult = lineService.findById(1);

        assertThat(actualResult)
                .isEmpty();
        verifyNoInteractions(lineMapper);
    }


    @Test
    void testFindBySeatIdLineExists() throws RepositoryException {
        Line line = getLine();
        LineDto lineDto = getLineDto();
        Integer seatId = 1;
        when(lineDao.findBySeatId(seatId))
                .thenReturn(Optional.of(line));
        when(lineMapper.mapFrom(line))
                .thenReturn(lineDto);

        final Optional<LineDto> actualResult = lineService.findBySeatId(seatId);

        assertThat(actualResult)
                .isPresent();
        assertThat(actualResult.get())
                .isEqualTo(lineDto);
    }

    @Test
    void testFindBySeatIdIfLineDoesNotExist() throws RepositoryException {
        Integer seatId = 1;
        when(lineDao.findBySeatId(seatId))
                .thenReturn(Optional.empty());

        final Optional<LineDto> actualResult = lineService.findBySeatId(seatId);

        assertThat(actualResult)
                .isEmpty();
        verifyNoInteractions(lineMapper);
    }

    @Test
    void testFindAll() throws RepositoryException {

        Line line = getLine();
        List<Line> lines = List.of(line);
        LineDto lineDto = getLineDto();
        when(lineDao.findAll())
                .thenReturn(lines);
        when(lineMapper.mapFrom(line))
                .thenReturn(lineDto);

        final List<LineDto> actualResult = lineService.findAll();

        assertThat(actualResult)
                .hasSize(1);
        actualResult.stream().map(LineDto::getId)
                .forEach(id -> assertThat(id).isEqualTo(1));
    }

    @Test
    void testFindAllByHallId() throws RepositoryException {

        Line line = getLine();
        List<Line> lines = List.of(line);
        LineDto lineDto = getLineDto();
        Integer hallId = 1;
        when(lineDao.findAllByHallId(hallId))
                .thenReturn(lines);
        when(lineMapper.mapFrom(line))
                .thenReturn(lineDto);

        final List<LineDto> actualResult = lineService.findAllByHallId(hallId);

        assertThat(actualResult)
                .hasSize(1);
        actualResult.stream().map(LineDto::getId)
                .forEach(id -> assertThat(id).isEqualTo(1));
    }
}
