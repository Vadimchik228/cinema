package com.vasche.service;

import com.vasche.dto.filter.ScreeningFilterDto;
import com.vasche.dto.movie.MovieDto;
import com.vasche.dto.screening.CreateScreeningDto;
import com.vasche.dto.screening.ScreeningAllDataDto;
import com.vasche.dto.screening.ScreeningDto;
import com.vasche.dto.screening.ScreeningWithHallDto;
import com.vasche.entity.Hall;
import com.vasche.entity.Movie;
import com.vasche.entity.Screening;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ServiceException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.movie.MovieMapper;
import com.vasche.mapper.screening.CreateScreeningMapper;
import com.vasche.mapper.screening.ScreeningAllDataMapper;
import com.vasche.mapper.screening.ScreeningMapper;
import com.vasche.mapper.screening.ScreeningWithHallMapper;
import com.vasche.repository.HallRepository;
import com.vasche.repository.MovieRepository;
import com.vasche.repository.ScreeningRepository;
import com.vasche.validator.CreateScreeningValidator;
import com.vasche.validator.ValidationResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScreeningServiceTest extends ServiceTestBase {

    private ScreeningService screeningService;
    private ScreeningRepository screeningRepository;
    private MovieRepository movieRepository;
    private HallRepository hallRepository;
    private MovieMapper movieMapper;
    private ScreeningMapper screeningMapper;
    private CreateScreeningValidator createScreeningValidator;
    private CreateScreeningMapper createScreeningMapper;
    private ScreeningAllDataMapper screeningAllDataMapper;
    private ScreeningWithHallMapper screeningWithHallMapper;

    @BeforeEach
    public void setUp() {
        createScreeningValidator = mock(CreateScreeningValidator.class);
        screeningRepository = mock(ScreeningRepository.class);
        movieRepository = mock(MovieRepository.class);
        hallRepository = mock(HallRepository.class);
        createScreeningMapper = mock(CreateScreeningMapper.class);
        screeningMapper = mock(ScreeningMapper.class);
        movieMapper = mock(MovieMapper.class);
        screeningAllDataMapper = mock(ScreeningAllDataMapper.class);
        screeningWithHallMapper = mock(ScreeningWithHallMapper.class);

        screeningService = new ScreeningService(createScreeningValidator,
                screeningRepository,
                movieRepository,
                hallRepository,
                createScreeningMapper,
                screeningMapper,
                movieMapper,
                screeningAllDataMapper,
                screeningWithHallMapper);
    }

    @Test
    public void testFindWithAllDataById() throws ServiceException, RepositoryException {
        Integer screeningId = 1;
        Screening screening = new Screening();
        Movie movie = new Movie();
        Hall hall = new Hall();
        when(screeningRepository.findById(screeningId)).thenReturn(Optional.of(screening));
        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(movie));
        when(hallRepository.findById(anyInt())).thenReturn(Optional.of(hall));
        when(screeningAllDataMapper.mapFrom(any(Screening.class), any(Movie.class), any(Hall.class)))
                .thenReturn(ScreeningAllDataDto.builder().build());

        var result = screeningService.findWithAllDataById(screeningId);

        assertTrue(result.isPresent());
        verify(screeningRepository).findById(screeningId);
        verify(movieRepository).findById(screening.getMovieId());
        verify(hallRepository).findById(screening.getHallId());
        verify(screeningAllDataMapper).mapFrom(screening, movie, hall);
    }

    @Test
    public void testFindByReservationId() throws ServiceException, RepositoryException {
        Integer reservationId = 1;
        Screening screening = new Screening();
        when(screeningRepository.findByReservationId(reservationId)).thenReturn(Optional.of(screening));
        when(screeningMapper.mapFrom(any(Screening.class))).thenReturn(ScreeningDto.builder().build());

        var result = screeningService.findByReservationId(reservationId);

        assertTrue(result.isPresent());
        verify(screeningRepository).findByReservationId(reservationId);
        verify(screeningMapper).mapFrom(screening);
    }

    @Test
    public void testFindAllAvailable() throws ServiceException, RepositoryException {

        Integer movieId = 1;
        List<Screening> screenings = new ArrayList<>();
        screenings.add(new Screening());
        when(screeningRepository.findAllAvailableByMovieId(movieId)).thenReturn(screenings);
        when(hallRepository.findById(anyInt())).thenReturn(Optional.of(new Hall()));
        when(screeningWithHallMapper.mapFrom(any(Screening.class), any(Hall.class)))
                .thenReturn(ScreeningWithHallDto.builder().build());

        var result = screeningService.findAllAvailable(movieId);

        assertFalse(result.isEmpty());
        verify(screeningRepository).findAllAvailableByMovieId(movieId);
        verify(hallRepository, times(screenings.size())).findById(anyInt());
        verify(screeningWithHallMapper, times(screenings.size())).mapFrom(any(Screening.class), any(Hall.class));
    }

    @Test
    public void testFindAllDisplayedMoviesByFilter() throws ServiceException, RepositoryException {

        ScreeningFilterDto filter = ScreeningFilterDto.builder().build();
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie());
        when(screeningRepository.findAllDisplayedMoviesByFilter(anyMap(), anyMap())).thenReturn(movies);
        when(movieMapper.mapFrom(any(Movie.class))).thenReturn(MovieDto.builder().build());

        var result = screeningService.findAllDisplayedMoviesByFilter(filter);

        assertFalse(result.isEmpty());
        verify(screeningRepository).findAllDisplayedMoviesByFilter(anyMap(), anyMap());
        verify(movieMapper, times(movies.size())).mapFrom(any(Movie.class));
    }

    @Test
    public void testFindAllWithAllDataByFilter() throws ServiceException, RepositoryException {
        ScreeningFilterDto filter = ScreeningFilterDto.builder().build();
        List<Screening> screenings = new ArrayList<>();
        screenings.add(new Screening());
        when(screeningRepository.findAllByFilter(anyMap(), anyMap())).thenReturn(screenings);
        when(movieRepository.findById(anyInt())).thenReturn(Optional.of(new Movie()));
        when(hallRepository.findById(anyInt())).thenReturn(Optional.of(new Hall()));
        when(screeningAllDataMapper.mapFrom(any(Screening.class), any(Movie.class), any(Hall.class))).thenReturn(ScreeningAllDataDto.builder().build());

        var result = screeningService.findAllWithAllDataByFilter(filter);

        assertFalse(result.isEmpty());
        verify(screeningRepository).findAllByFilter(anyMap(), anyMap());
        verify(movieRepository, times(screenings.size())).findById(anyInt());
        verify(hallRepository, times(screenings.size())).findById(anyInt());
        verify(screeningAllDataMapper, times(screenings.size())).mapFrom(any(Screening.class), any(Movie.class), any(Hall.class));
    }

    @Test
    public void testCountNumberOfScreenings() throws ServiceException, RepositoryException {
        List<Screening> screenings = new ArrayList<>();
        screenings.add(new Screening());
        when(screeningRepository.findAll()).thenReturn(screenings);

        Integer count = screeningService.countNumberOfScreenings();

        assertNotNull(count);
        assertEquals(Integer.valueOf(screenings.size()), count);
        verify(screeningRepository).findAll();
    }

    @Test
    void testCreateIfValidationPassed() throws RepositoryException, ServiceException {

        CreateScreeningDto createScreeningDto = getCreateScreeningDto();
        Screening screening = getScreening(1);
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        doReturn(validationResult).when(createScreeningValidator).isValid(createScreeningDto);

        when(validationResult.isValid())
                .thenReturn(true);
        doReturn(screening).when(createScreeningMapper).mapFrom(createScreeningDto);

        doReturn(screening).when(screeningRepository).save(screening);

        Integer actualResult = screeningService.create(createScreeningDto);

        Assertions.assertThat(actualResult)
                .isEqualTo(1);
    }

    @Test
    void testCreateIfValidationFailed() {
        CreateScreeningDto createScreeningDto = getCreateScreeningDto();
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        when(createScreeningValidator.isValid(createScreeningDto))
                .thenReturn(validationResult);
        when(validationResult.isValid())
                .thenReturn(false);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> screeningService.create(createScreeningDto));
    }

    @Test
    void testUpdateIfValidationFailed() {
        CreateScreeningDto createScreeningDto = getCreateScreeningDto();
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        when(createScreeningValidator.isValid(createScreeningDto))
                .thenReturn(validationResult);
        when(validationResult.isValid())
                .thenReturn(false);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> screeningService.create(createScreeningDto));
    }

    @Test
    void testDelete() throws RepositoryException, ServiceException {
        when(screeningRepository.delete(1))
                .thenReturn(true);

        boolean actualResult = screeningService.delete(1);

        assertThat(actualResult).isTrue();
    }

    @Test
    void testFindByIdIfScreeningExists() throws RepositoryException, ServiceException {
        Screening screening = getScreening(1);
        ScreeningDto screeningDto = getScreeningDto(1);
        when(screeningRepository.findById(screening.getId()))
                .thenReturn(Optional.of(screening));
        when(screeningMapper.mapFrom(screening))
                .thenReturn(screeningDto);

        final Optional<ScreeningDto> actualResult = screeningService.findById(screening.getId());

        assertThat(actualResult)
                .isPresent();
        assertThat(actualResult.get())
                .isEqualTo(screeningDto);
    }

    @Test
    void testFindByIdIfScreeningDoesNotExist() throws RepositoryException, ServiceException {
        when(screeningRepository.findById(1))
                .thenReturn(Optional.empty());

        final Optional<ScreeningDto> actualResult = screeningService.findById(1);

        assertThat(actualResult)
                .isEmpty();
        verifyNoInteractions(screeningMapper);
    }

    @Test
    void testFindByReservationIdIfScreeningExists() throws RepositoryException, ServiceException {
        Screening screening = getScreening(1);
        ScreeningDto screeningDto = getScreeningDto(1);
        Integer reservationId = 1;
        when(screeningRepository.findByReservationId(reservationId))
                .thenReturn(Optional.of(screening));
        when(screeningMapper.mapFrom(screening))
                .thenReturn(screeningDto);

        final Optional<ScreeningDto> actualResult = screeningService.findByReservationId(reservationId);

        assertThat(actualResult)
                .isPresent();
        assertThat(actualResult.get())
                .isEqualTo(screeningDto);
    }

    @Test
    void testFindByReservationIdIfScreeningDoesNotExist() throws RepositoryException, ServiceException {
        Integer reservationId = 1;
        when(screeningRepository.findByReservationId(reservationId))
                .thenReturn(Optional.empty());

        final Optional<ScreeningDto> actualResult = screeningService.findByReservationId(reservationId);

        assertThat(actualResult)
                .isEmpty();
        verifyNoInteractions(screeningMapper);
    }

    @Test
    void testFindAll() throws RepositoryException, ServiceException {

        Screening screening = getScreening(1);
        List<Screening> screenings = List.of(screening);
        ScreeningDto screeningDto = getScreeningDto(1);
        when(screeningRepository.findAll())
                .thenReturn(screenings);
        when(screeningMapper.mapFrom(screening))
                .thenReturn(screeningDto);

        final List<ScreeningDto> actualResult = screeningService.findAll();

        assertThat(actualResult)
                .hasSize(1);
        actualResult.stream().map(ScreeningDto::getId)
                .forEach(id -> assertThat(id).isEqualTo(1));
    }

}
