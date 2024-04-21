package com.vasche.service;

import com.vasche.repository.ReservationRepository;
import com.vasche.dto.reservation.CreateReservationDto;
import com.vasche.dto.reservation.ReservationDto;
import com.vasche.entity.Reservation;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.reservation.CreateReservationMapper;
import com.vasche.mapper.reservation.ReservationMapper;
import com.vasche.validator.CreateReservationValidator;
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
public class ReservationServiceTest extends ServiceTestBase {

    @Mock
    private CreateReservationValidator createReservationValidator;

    @Mock
    private ReservationRepository reservationDao;

    @Mock
    private CreateReservationMapper createReservationMapper;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void testCreateIfValidationPassed() throws RepositoryException {

        CreateReservationDto createReservationDto = getCreateReservationDto();
        Reservation reservation = getReservation();
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        doReturn(validationResult).when(createReservationValidator).isValid(createReservationDto);

        when(validationResult.isValid())
                .thenReturn(true);
        doReturn(reservation).when(createReservationMapper).mapFrom(createReservationDto);

        doReturn(reservation).when(reservationDao).save(reservation);

        Integer actualResult = reservationService.create(createReservationDto);

        assertThat(actualResult)
                .isEqualTo(1);
    }

    @Test
    void testCreateIfValidationFailed() {
        CreateReservationDto createReservationDto = getCreateReservationDto();
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        when(createReservationValidator.isValid(createReservationDto))
                .thenReturn(validationResult);
        when(validationResult.isValid())
                .thenReturn(false);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> reservationService.create(createReservationDto));
    }

    @Test
    void testDelete() throws RepositoryException {
        when(reservationDao.delete(1))
                .thenReturn(true);

        boolean actualResult = reservationService.delete(1);

        assertThat(actualResult).isTrue();
    }

    @Test
    void testFindByIdIfReservationExists() throws RepositoryException {
        Reservation reservation = getReservation();
        ReservationDto reservationDto = getReservationDto();
        when(reservationDao.findById(reservation.getId()))
                .thenReturn(Optional.of(reservation));
        when(reservationMapper.mapFrom(reservation))
                .thenReturn(reservationDto);

        final Optional<ReservationDto> actualResult = reservationService.findById(reservation.getId());

        assertThat(actualResult)
                .isPresent();
        assertThat(actualResult.get())
                .isEqualTo(reservationDto);
    }

    @Test
    void testFindByIdIfReservationDoesNotExist() throws RepositoryException {
        when(reservationDao.findById(1))
                .thenReturn(Optional.empty());

        final Optional<ReservationDto> actualResult = reservationService.findById(1);

        assertThat(actualResult)
                .isEmpty();
        verifyNoInteractions(reservationMapper);
    }

    @Test
    void testFindAll() throws RepositoryException {

        Reservation reservation = getReservation();
        List<Reservation> reservations = List.of(reservation);
        ReservationDto reservationDto = getReservationDto();
        when(reservationDao.findAll())
                .thenReturn(reservations);
        when(reservationMapper.mapFrom(reservation))
                .thenReturn(reservationDto);

        final List<ReservationDto> actualResult = reservationService.findAll();

        assertThat(actualResult)
                .hasSize(1);
        actualResult.stream().map(ReservationDto::getId)
                .forEach(id -> assertThat(id).isEqualTo(1));
    }

    @Test
    void testFindAllByScreeningId() throws RepositoryException {

        Reservation reservation = getReservation();
        List<Reservation> reservations = List.of(reservation);
        ReservationDto reservationDto = getReservationDto();
        Integer screeningId = 1;
        when(reservationDao.findAllByScreeningId(screeningId))
                .thenReturn(reservations);
        when(reservationMapper.mapFrom(reservation))
                .thenReturn(reservationDto);

        final List<ReservationDto> actualResult = reservationService.findAllByScreeningId(screeningId);

        assertThat(actualResult)
                .hasSize(1);
        actualResult.stream().map(ReservationDto::getId)
                .forEach(id -> assertThat(id).isEqualTo(1));
    }

    @Test
    void testFindAllByUserId() throws RepositoryException {

        Reservation reservation = getReservation();
        List<Reservation> reservations = List.of(reservation);
        ReservationDto reservationDto = getReservationDto();
        Integer userId = 1;
        when(reservationDao.findAllByUserId(userId))
                .thenReturn(reservations);
        when(reservationMapper.mapFrom(reservation))
                .thenReturn(reservationDto);

        final List<ReservationDto> actualResult = reservationService.findAllByUserId(userId);

        assertThat(actualResult)
                .hasSize(1);
        actualResult.stream().map(ReservationDto::getId)
                .forEach(id -> assertThat(id).isEqualTo(1));
    }

}
