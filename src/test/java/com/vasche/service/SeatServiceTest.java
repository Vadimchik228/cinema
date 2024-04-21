package com.vasche.service;


import com.vasche.repository.SeatRepository;
import com.vasche.dto.seat.CreateSeatDto;
import com.vasche.dto.seat.SeatDto;
import com.vasche.entity.Seat;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.seat.CreateSeatMapper;
import com.vasche.mapper.seat.SeatMapper;
import com.vasche.validator.CreateSeatValidator;
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
public class SeatServiceTest extends ServiceTestBase {

    @Mock
    private CreateSeatValidator createSeatValidator;

    @Mock
    private SeatRepository seatDao;

    @Mock
    private CreateSeatMapper createSeatMapper;

    @Mock
    private SeatMapper seatMapper;

    @InjectMocks
    private SeatService seatService;

    @Test
    void testCreateIfValidationPassed() throws RepositoryException {

        CreateSeatDto createSeatDto = getCreateSeatDto();
        Seat seat = getSeat();
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        doReturn(validationResult).when(createSeatValidator).isValid(createSeatDto);

        when(validationResult.isValid())
                .thenReturn(true);
        doReturn(seat).when(createSeatMapper).mapFrom(createSeatDto);

        doReturn(seat).when(seatDao).save(seat);

        Integer actualResult = seatService.create(createSeatDto);

        assertThat(actualResult)
                .isEqualTo(1);
    }

    @Test
    void testCreateIfValidationFailed() {
        CreateSeatDto createSeatDto = getCreateSeatDto();
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        when(createSeatValidator.isValid(createSeatDto))
                .thenReturn(validationResult);
        when(validationResult.isValid())
                .thenReturn(false);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> seatService.create(createSeatDto));
    }

    @Test
    void testUpdateIfValidationFailed() {
        CreateSeatDto createSeatDto = getCreateSeatDto();
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        when(createSeatValidator.isValid(createSeatDto))
                .thenReturn(validationResult);
        when(validationResult.isValid())
                .thenReturn(false);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> seatService.create(createSeatDto));
    }

    @Test
    void testDelete() throws RepositoryException {
        when(seatDao.delete(1))
                .thenReturn(true);

        boolean actualResult = seatService.delete(1);

        assertThat(actualResult).isTrue();
    }

    @Test
    void testFindByIdIfSeatExists() throws RepositoryException {
        Seat seat = getSeat();
        SeatDto seatDto = getSeatDto();
        when(seatDao.findById(seat.getId()))
                .thenReturn(Optional.of(seat));
        when(seatMapper.mapFrom(seat))
                .thenReturn(seatDto);

        final Optional<SeatDto> actualResult = seatService.findById(seat.getId());

        assertThat(actualResult)
                .isPresent();
        assertThat(actualResult.get())
                .isEqualTo(seatDto);
    }

    @Test
    void testFindByIdIfSeatDoesNotExist() throws RepositoryException {
        when(seatDao.findById(1))
                .thenReturn(Optional.empty());

        final Optional<SeatDto> actualResult = seatService.findById(1);

        assertThat(actualResult)
                .isEmpty();
        verifyNoInteractions(seatMapper);
    }


    @Test
    void testFindByReservationIdIfSeatExists() throws RepositoryException {
        Seat seat = getSeat();
        SeatDto seatDto = getSeatDto();
        Integer reservationId = 1;
        when(seatDao.findByReservationId(reservationId))
                .thenReturn(Optional.of(seat));
        when(seatMapper.mapFrom(seat))
                .thenReturn(seatDto);

        final Optional<SeatDto> actualResult = seatService.findByReservationId(reservationId);

        assertThat(actualResult)
                .isPresent();
        assertThat(actualResult.get())
                .isEqualTo(seatDto);
    }

    @Test
    void testFindByReservationIdIfSeatDoesNotExist() throws RepositoryException {
        Integer reservationId = 1;
        when(seatDao.findByReservationId(reservationId))
                .thenReturn(Optional.empty());

        final Optional<SeatDto> actualResult = seatService.findByReservationId(reservationId);

        assertThat(actualResult)
                .isEmpty();
        verifyNoInteractions(seatMapper);
    }

    @Test
    void testFindAll() throws RepositoryException {

        Seat seat = getSeat();
        List<Seat> seats = List.of(seat);
        SeatDto seatDto = getSeatDto();
        when(seatDao.findAll())
                .thenReturn(seats);
        when(seatMapper.mapFrom(seat))
                .thenReturn(seatDto);

        final List<SeatDto> actualResult = seatService.findAll();

        assertThat(actualResult)
                .hasSize(1);
        actualResult.stream().map(SeatDto::getId)
                .forEach(id -> assertThat(id).isEqualTo(1));
    }

    @Test
    void testFindAllByLineId() throws RepositoryException {

        Seat seat = getSeat();
        List<Seat> seats = List.of(seat);
        SeatDto seatDto = getSeatDto();
        Integer lineId = 1;
        when(seatDao.findAllByLineId(lineId))
                .thenReturn(seats);
        when(seatMapper.mapFrom(seat))
                .thenReturn(seatDto);

        final List<SeatDto> actualResult = seatService.findAllByLineId(lineId);

        assertThat(actualResult)
                .hasSize(1);
        actualResult.stream().map(SeatDto::getId)
                .forEach(id -> assertThat(id).isEqualTo(1));
    }

    @Test
    void testFindAllByHallId() throws RepositoryException {

        Seat seat = getSeat();
        List<Seat> seats = List.of(seat);
        SeatDto seatDto = getSeatDto();
        Integer hallId = 1;
        when(seatDao.findAllByHallId(hallId))
                .thenReturn(seats);
        when(seatMapper.mapFrom(seat))
                .thenReturn(seatDto);

        final List<SeatDto> actualResult = seatService.findAllByHallId(hallId);

        assertThat(actualResult)
                .hasSize(1);
        actualResult.stream().map(SeatDto::getId)
                .forEach(id -> assertThat(id).isEqualTo(1));
    }

    @Test
    void testFindAllAvailable() throws RepositoryException {

        Seat seat = getSeat();
        List<Seat> seats = List.of(seat);
        SeatDto seatDto = getSeatDto();
        Integer screeningId = 1;
        Integer hallId = 1;
        when(seatDao.findAllAvailable(screeningId, hallId))
                .thenReturn(seats);
        when(seatMapper.mapFrom(seat))
                .thenReturn(seatDto);

        final List<SeatDto> actualResult = seatService.findAllAvailable(screeningId, hallId);

        assertThat(actualResult)
                .hasSize(1);
        actualResult.stream().map(SeatDto::getId)
                .forEach(id -> assertThat(id).isEqualTo(1));
    }

}