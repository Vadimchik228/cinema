package com.vasche.service;


import com.vasche.dto.seat.SeatAllDataDto;
import com.vasche.dto.seat.SeatDto;
import com.vasche.entity.Seat;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ServiceException;
import com.vasche.mapper.seat.SeatAllDataMapper;
import com.vasche.mapper.seat.SeatMapper;
import com.vasche.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;
    @Mock
    private SeatMapper seatMapper;
    @Mock
    private SeatAllDataMapper seatAllDataMapper;

    @InjectMocks
    private SeatService seatService;

    @Test
    void findById() throws Exception {
        Seat seat = new Seat();
        Optional<Seat> seatOptional = Optional.of(seat);
        when(seatRepository.findById(any())).thenReturn(seatOptional);
        when(seatMapper.mapFrom(seat)).thenReturn(SeatDto.builder().build());

        Optional<SeatDto> result = seatService.findById(1);

        assertThat(result).isPresent();
        verify(seatRepository).findById(1);
        verify(seatMapper).mapFrom(seat);
    }

    @Test
    void findById_shouldThrowServiceException() throws RepositoryException {
        when(seatRepository.findById(any())).thenThrow(RepositoryException.class);

        assertThrows(ServiceException.class, () -> seatService.findById(1));
        verify(seatRepository).findById(1);
        verifyNoInteractions(seatMapper);
    }

    @Test
    void findByReservationId() throws Exception {
        Seat seat = new Seat();
        Optional<Seat> seatOptional = Optional.of(seat);
        when(seatRepository.findByReservationId(any())).thenReturn(seatOptional);
        when(seatMapper.mapFrom(seat)).thenReturn(SeatDto.builder().build());

        Optional<SeatDto> result = seatService.findByReservationId(1);

        assertThat(result).isPresent();
        verify(seatRepository).findByReservationId(1);
        verify(seatMapper).mapFrom(seat);
    }

    @Test
    void findByReservationId_shouldThrowServiceException() throws RepositoryException {
        when(seatRepository.findByReservationId(any())).thenThrow(RepositoryException.class);

        assertThrows(ServiceException.class, () -> seatService.findByReservationId(1));
        verify(seatRepository).findByReservationId(1);
        verifyNoInteractions(seatMapper);
    }

    @Test
    void findAll() throws Exception {
        List<Seat> seats = List.of(new Seat(), new Seat(), new Seat());
        when(seatRepository.findAll()).thenReturn(seats);
        when(seatMapper.mapFrom(any())).thenReturn(SeatDto.builder().build());

        List<SeatDto> result = seatService.findAll();

        assertThat(result).hasSize(3);
        verify(seatRepository).findAll();
        verify(seatMapper, times(3)).mapFrom(any());
    }

    @Test
    void findAll_shouldThrowServiceException() throws RepositoryException {
        when(seatRepository.findAll()).thenThrow(RepositoryException.class);

        assertThrows(ServiceException.class, () -> seatService.findAll());
        verify(seatRepository).findAll();
        verifyNoInteractions(seatMapper);
    }

    @Test
    void findAllWithAllDataByLineId() throws Exception {
        List<Seat> seats = List.of(new Seat(), new Seat(), new Seat());
        when(seatRepository.findAllByLineId(any())).thenReturn(seats);
        when(seatAllDataMapper.mapFrom(any(), any())).thenReturn(SeatAllDataDto.builder().build());
        when(seatRepository.findAllAvailableByLineId(any(), any())).thenReturn(Collections.emptyList());

        List<SeatAllDataDto> result = seatService.findAllWithAllDataByLineId(1, 1);

        assertThat(result).hasSize(3);
        verify(seatRepository).findAllByLineId(1);
        verify(seatAllDataMapper, times(3)).mapFrom(any(), any());
        verify(seatRepository).findAllAvailableByLineId(1, 1);
    }
}