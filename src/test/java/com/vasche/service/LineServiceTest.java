package com.vasche.service;

import com.vasche.dto.line.LineAllDataDto;
import com.vasche.dto.line.LineDto;
import com.vasche.entity.Line;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ServiceException;
import com.vasche.mapper.line.LineAllDataMapper;
import com.vasche.mapper.line.LineMapper;
import com.vasche.repository.LineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineRepository lineRepository;
    @Mock
    private LineMapper lineMapper;
    @Mock
    private LineAllDataMapper lineAllDataMapper;
    @Mock
    private SeatService seatService;
    @InjectMocks
    private LineService lineService;

    @Test
    void findById() throws Exception {
        Line line = new Line();
        Optional<Line> lineOptional = Optional.of(line);
        when(lineRepository.findById(any())).thenReturn(lineOptional);
        when(lineMapper.mapFrom(line)).thenReturn(LineDto.builder().build());

        Optional<LineDto> result = lineService.findById(1);

        assertThat(result).isPresent();
        verify(lineRepository).findById(1);
        verify(lineMapper).mapFrom(line);
    }

    @Test
    void findById_shouldThrowServiceException() throws RepositoryException {
        when(lineRepository.findById(any())).thenThrow(RepositoryException.class);

        assertThrows(ServiceException.class, () -> lineService.findById(1));
        verify(lineRepository).findById(1);
        verifyNoInteractions(lineMapper);
    }

    @Test
    void findAll() throws Exception {
        List<Line> lines = List.of(new Line(), new Line(), new Line());
        when(lineRepository.findAll()).thenReturn(lines);
        when(lineMapper.mapFrom(any())).thenReturn(LineDto.builder().build());

        List<LineDto> result = lineService.findAll();

        assertThat(result).hasSize(3);
        verify(lineRepository).findAll();
        verify(lineMapper, times(3)).mapFrom(any());
    }

    @Test
    void findAll_shouldThrowServiceException() throws RepositoryException {
        when(lineRepository.findAll()).thenThrow(RepositoryException.class);

        assertThrows(ServiceException.class, () -> lineService.findAll());
        verify(lineRepository).findAll();
        verifyNoInteractions(lineMapper);
    }

    @Test
    void findAllWithAllDataByHallId() throws Exception {
        List<Line> lines = List.of(new Line(), new Line(), new Line());
        when(lineRepository.findAllByHallId(any())).thenReturn(lines);
        when(lineAllDataMapper.mapFrom(any(), any())).thenReturn(LineAllDataDto.builder().build());
        when(seatService.findAllWithAllDataByLineId(any(), any())).thenReturn(List.of());

        List<LineAllDataDto> result = lineService.findAllWithAllDataByHallId(1, 1);


        assertThat(result).hasSize(3);
        verify(lineRepository).findAllByHallId(1);
        verify(lineAllDataMapper, times(3)).mapFrom(any(), any());
        verify(seatService, times(3)).findAllWithAllDataByLineId(any(), any());
    }

    @Test
    void findAllWithAllDataByHallId_shouldThrowServiceException() throws RepositoryException {
        when(lineRepository.findAllByHallId(any())).thenThrow(RepositoryException.class);

        assertThrows(ServiceException.class, () -> lineService.findAllWithAllDataByHallId(1, 1));
        verify(lineRepository).findAllByHallId(1);
        verifyNoInteractions(lineAllDataMapper);
        verifyNoInteractions(seatService);
    }
}

