package com.vasche.service;

import com.vasche.dto.reservation.CreateReservationDto;
import com.vasche.dto.reservation.ReservationAllDataDto;
import com.vasche.dto.reservation.ReservationDto;
import com.vasche.entity.*;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ReservationAllDataException;
import com.vasche.exception.ServiceException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.reservation.CreateReservationMapper;
import com.vasche.mapper.reservation.ReservationAllDataMapper;
import com.vasche.mapper.reservation.ReservationMapper;
import com.vasche.repository.*;
import com.vasche.validator.CreateReservationValidator;
import com.vasche.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ScreeningRepository screeningRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private HallRepository hallRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private ReservationAllDataMapper reservationAllDataMapper;

    @Mock
    private CreateReservationMapper createReservationMapper;

    @Mock
    private CreateReservationValidator createReservationValidator;

    @InjectMocks
    private ReservationService reservationService;

    ReservationServiceTest() {
    }

    @Test
    void testCreate() throws ServiceException, ValidationException, RepositoryException {
        CreateReservationDto createReservationDto = CreateReservationDto.builder().build();
        ValidationResult validationResult = new ValidationResult();
        Reservation reservation = new Reservation();
        reservation.setId(1);

        when(createReservationValidator.isValid(any())).thenReturn(validationResult);
        when(createReservationMapper.mapFrom(any())).thenReturn(reservation);
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Integer result = reservationService.create(createReservationDto);

        assertEquals(1, result);
        verify(createReservationValidator, times(1)).isValid(any());
        verify(createReservationMapper, times(1)).mapFrom(any());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testFindById() throws RepositoryException, ServiceException {
        Reservation reservation = new Reservation();
        reservation.setId(2);
        ReservationDto reservationDto = ReservationDto.builder().build();

        when(reservationRepository.findById(2)).thenReturn(Optional.of(reservation));
        when(reservationMapper.mapFrom(reservation)).thenReturn(reservationDto);

        Optional<ReservationDto> result = reservationService.findById(2);

        result.ifPresent(dto -> assertEquals(reservationDto, dto));
        verify(reservationRepository, times(1)).findById(2);
        verify(reservationMapper, times(1)).mapFrom(reservation);
    }

    @Test
    void testGetReservationAllDataDto() throws RepositoryException, ReservationAllDataException {
        Reservation reservation = new Reservation();
        reservation.setUserId(1);
        reservation.setScreeningId(1);
        reservation.setSeatId(1);
        User user = new User();
        Screening screening = new Screening();
        Seat seat = new Seat();
        Line line = new Line();
        Movie movie = new Movie();
        Hall hall = new Hall();
        ReservationAllDataDto expected = ReservationAllDataDto.builder().build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(screeningRepository.findById(1)).thenReturn(Optional.of(screening));
        when(seatRepository.findById(1)).thenReturn(Optional.of(seat));
        when(movieRepository.findById(screening.getMovieId())).thenReturn(Optional.of(movie));
        when(hallRepository.findById(screening.getHallId())).thenReturn(Optional.of(hall));
        when(lineRepository.findById(seat.getLineId())).thenReturn(Optional.of(line));
        when(reservationAllDataMapper.mapFrom(reservation, user, screening, movie, hall, line, seat))
                .thenReturn(expected);

        ReservationAllDataDto result = reservationService.getReservationAllDataDto(reservation);

        assertEquals(expected, result);
        verify(userRepository, times(1)).findById(1);
        verify(screeningRepository, times(1)).findById(1);
        verify(seatRepository, times(1)).findById(1);
        verify(movieRepository, times(1)).findById(screening.getMovieId());
        verify(hallRepository, times(1)).findById(screening.getHallId());
        verify(lineRepository, times(1)).findById(seat.getLineId());
        verify(reservationAllDataMapper, times(1)).mapFrom(reservation, user, screening, movie, hall, line, seat);
    }

    @Test
    void testDelete() throws RepositoryException, ServiceException {
        when(reservationRepository.delete(6)).thenReturn(true);

        boolean result = reservationService.delete(6);

        assertTrue(result);
        verify(reservationRepository, times(1)).delete(6);
    }

    @Test
    void testCountTotalIncome() throws RepositoryException, ServiceException {
        Reservation reservation = new Reservation();
        reservation.setScreeningId(1);
        Screening screening = new Screening();
        screening.setPrice(BigDecimal.TEN);
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);

        when(reservationRepository.findAll()).thenReturn(reservations);
        when(screeningRepository.findById(1)).thenReturn(Optional.of(screening));

        BigDecimal result = reservationService.countTotalIncome();

        assertEquals(BigDecimal.TEN, result);
        verify(reservationRepository, times(1)).findAll();
        verify(screeningRepository, times(1)).findById(1);
    }

    @Test
    void testCountIncomeByScreeningId() throws RepositoryException, ServiceException {
        Reservation reservation1 = new Reservation();
        reservation1.setScreeningId(2);
        Reservation reservation2 = new Reservation();
        reservation2.setScreeningId(2);
        Screening screening = new Screening();
        screening.setPrice(BigDecimal.TEN);
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation1);
        reservations.add(reservation2);

        when(reservationRepository.findAll()).thenReturn(reservations);
        when(screeningRepository.findById(2)).thenReturn(Optional.of(screening));

        BigDecimal result = reservationService.countIncomeByScreeningId(2);

        assertEquals(BigDecimal.valueOf(20), result);
        verify(reservationRepository, times(1)).findAll();
        verify(screeningRepository, times(2)).findById(2);
    }

}

