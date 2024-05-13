package com.vasche.service;

import com.vasche.dto.hall.HallAllDataDto;
import com.vasche.dto.hall.HallDto;
import com.vasche.entity.Hall;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ServiceException;
import com.vasche.mapper.hall.HallAllDataMapper;
import com.vasche.mapper.hall.HallMapper;
import com.vasche.repository.HallRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HallServiceTest {

    @Mock
    private HallRepository hallRepository;
    @Mock
    private HallMapper hallMapper;
    @Mock
    private HallAllDataMapper hallAllDataMapper;
    @Mock
    private LineService lineService;

    @InjectMocks
    private HallService hallService;

    @Test
    void findById() throws Exception {
        Hall hall = new Hall();
        Optional<Hall> hallOptional = Optional.of(hall);
        when(hallRepository.findById(any())).thenReturn(hallOptional);
        when(hallMapper.mapFrom(hall)).thenReturn(HallDto.builder().build());

        Optional<HallDto> result = hallService.findById(1);

        assertThat(result).isPresent();
        verify(hallRepository).findById(1);
        verify(hallMapper).mapFrom(hall);
    }

    @Test
    void findById_shouldThrowServiceException() throws RepositoryException {
        when(hallRepository.findById(any())).thenThrow(RepositoryException.class);

        assertThrows(ServiceException.class, () -> hallService.findById(1));
        verify(hallRepository).findById(1);
        verifyNoInteractions(hallMapper);
    }

    @Test
    void findAll() throws Exception {
        List<Hall> halls = List.of(new Hall(), new Hall(), new Hall());
        when(hallRepository.findAll()).thenReturn(halls);
        when(hallMapper.mapFrom(any())).thenReturn(HallDto.builder().build());

        List<HallDto> result = hallService.findAll();

        assertThat(result).hasSize(3);
        verify(hallRepository).findAll();
        verify(hallMapper, times(3)).mapFrom(any());
    }

    @Test
    void findAll_shouldThrowServiceException() throws RepositoryException {
        when(hallRepository.findAll()).thenThrow(RepositoryException.class);

        assertThrows(ServiceException.class, () -> hallService.findAll());
        verify(hallRepository).findAll();
        verifyNoInteractions(hallMapper);
    }

    @Test
    void findAllWithAllData() throws Exception {
        List<Hall> halls = List.of(new Hall(), new Hall(), new Hall());
        when(hallRepository.findAll()).thenReturn(halls);
        when(hallAllDataMapper.mapFrom(any(), any())).thenReturn(HallAllDataDto.builder().build());
        when(lineService.findAllWithAllDataByHallId(any(), any())).thenReturn(List.of());

        List<HallAllDataDto> result = hallService.findAllWithAllData(1);

        assertThat(result).hasSize(3);
        verify(hallRepository).findAll();
        verify(hallAllDataMapper, times(3)).mapFrom(any(), any());
        verify(lineService, times(3)).findAllWithAllDataByHallId(any(), any());
    }

    @Test
    void findAllWithAllData_shouldThrowServiceException() throws RepositoryException {
        when(hallRepository.findAll()).thenThrow(RepositoryException.class);

        assertThrows(ServiceException.class, () -> hallService.findAllWithAllData(1));
        verify(hallRepository).findAll();
        verifyNoInteractions(hallAllDataMapper);
        verifyNoInteractions(lineService);
    }

    @Test
    void findWithAllDataById_shouldThrowServiceException() throws RepositoryException {
        when(hallRepository.findById(any())).thenThrow(RepositoryException.class);

        assertThrows(ServiceException.class, () -> hallService.findWithAllDataById(1, 1));
        verify(hallRepository).findById(1);
        verifyNoInteractions(hallAllDataMapper);
        verifyNoInteractions(lineService);
    }

    @Test
    void countNumberOfHalls() throws Exception {
        List<Hall> halls = List.of(new Hall(), new Hall(), new Hall());
        when(hallRepository.findAll()).thenReturn(halls);

        int result = hallService.countNumberOfHalls();

        assertThat(result).isEqualTo(3);
        verify(hallRepository).findAll();
    }

    @Test
    void countNumberOfHalls_shouldThrowServiceException() throws RepositoryException {
        when(hallRepository.findAll()).thenThrow(RepositoryException.class);
        assertThrows(ServiceException.class, () -> hallService.countNumberOfHalls());
        verify(hallRepository).findAll();
    }
}