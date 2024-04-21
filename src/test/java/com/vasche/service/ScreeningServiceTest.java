package com.vasche.service;

import com.vasche.repository.ScreeningRepository;
import com.vasche.dto.filter.ScreeningFilterDto;
import com.vasche.dto.movie.MovieDto;
import com.vasche.dto.screening.CreateScreeningDto;
import com.vasche.dto.screening.ScreeningDto;
import com.vasche.entity.Movie;
import com.vasche.entity.Screening;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.movie.MovieMapper;
import com.vasche.mapper.screening.CreateScreeningMapper;
import com.vasche.mapper.screening.ScreeningMapper;
import com.vasche.validator.CreateScreeningValidator;
import com.vasche.validator.ValidationResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.vasche.util.constants.FilteredAttributes.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScreeningServiceTest extends ServiceTestBase {

    @Mock
    private CreateScreeningValidator createScreeningValidator;
    @Mock
    private ScreeningRepository screeningDao;
    @Mock
    private CreateScreeningMapper createScreeningMapper;
    @Mock
    private ScreeningMapper screeningMapper;
    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private ScreeningService screeningService = new ScreeningService();

    @Test
    void testCreateIfValidationPassed() throws RepositoryException {

        CreateScreeningDto createScreeningDto = getCreateScreeningDto();
        Screening screening = getScreening(1);
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        doReturn(validationResult).when(createScreeningValidator).isValid(createScreeningDto);

        when(validationResult.isValid())
                .thenReturn(true);
        doReturn(screening).when(createScreeningMapper).mapFrom(createScreeningDto);

        doReturn(screening).when(screeningDao).save(screening);

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
    void testDelete() throws RepositoryException {
        when(screeningDao.delete(1))
                .thenReturn(true);

        boolean actualResult = screeningService.delete(1);

        assertThat(actualResult).isTrue();
    }

    @Test
    void testFindByIdIfScreeningExists() throws RepositoryException {
        Screening screening = getScreening(1);
        ScreeningDto screeningDto = getScreeningDto(1);
        when(screeningDao.findById(screening.getId()))
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
    void testFindByIdIfScreeningDoesNotExist() throws RepositoryException {
        when(screeningDao.findById(1))
                .thenReturn(Optional.empty());

        final Optional<ScreeningDto> actualResult = screeningService.findById(1);

        assertThat(actualResult)
                .isEmpty();
        verifyNoInteractions(screeningMapper);
    }

    @Test
    void testFindByReservationIdIfScreeningExists() throws RepositoryException {
        Screening screening = getScreening(1);
        ScreeningDto screeningDto = getScreeningDto(1);
        Integer reservationId = 1;
        when(screeningDao.findByReservationId(reservationId))
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
    void testFindByReservationIdIfScreeningDoesNotExist() throws RepositoryException {
        Integer reservationId = 1;
        when(screeningDao.findByReservationId(reservationId))
                .thenReturn(Optional.empty());

        final Optional<ScreeningDto> actualResult = screeningService.findByReservationId(reservationId);

        assertThat(actualResult)
                .isEmpty();
        verifyNoInteractions(screeningMapper);
    }

    @Test
    void testFindAll() throws RepositoryException {

        Screening screening = getScreening(1);
        List<Screening> screenings = List.of(screening);
        ScreeningDto screeningDto = getScreeningDto(1);
        when(screeningDao.findAll())
                .thenReturn(screenings);
        when(screeningMapper.mapFrom(screening))
                .thenReturn(screeningDto);

        final List<ScreeningDto> actualResult = screeningService.findAll();

        assertThat(actualResult)
                .hasSize(1);
        actualResult.stream().map(ScreeningDto::getId)
                .forEach(id -> assertThat(id).isEqualTo(1));
    }

    @Test
    void testFindAllByUserId() throws RepositoryException {

        Screening screening = getScreening(1);
        List<Screening> screenings = List.of(screening);
        ScreeningDto screeningDto = getScreeningDto(1);
        Integer userId = 1;
        when(screeningDao.findAllByUserId(userId))
                .thenReturn(screenings);
        when(screeningMapper.mapFrom(screening))
                .thenReturn(screeningDto);

        final List<ScreeningDto> actualResult = screeningService.findAllByUserId(userId);

        assertThat(actualResult)
                .hasSize(1);
        actualResult.stream().map(ScreeningDto::getId)
                .forEach(id -> assertThat(id).isEqualTo(1));
    }

    @Test
    void testFindAllByMovieId() throws RepositoryException {

        Screening screening = getScreening(1);
        List<Screening> screenings = List.of(screening);
        ScreeningDto screeningDto = getScreeningDto(1);
        Integer movieId = 1;
        when(screeningDao.findAllByMovieId(movieId))
                .thenReturn(screenings);
        when(screeningMapper.mapFrom(screening))
                .thenReturn(screeningDto);

        final List<ScreeningDto> actualResult = screeningService.findAllByMovieId(movieId);

        assertThat(actualResult)
                .hasSize(1);
        actualResult.stream().map(ScreeningDto::getId)
                .forEach(id -> assertThat(id).isEqualTo(1));
    }

    @Test
    void testFindAllAvailable() throws RepositoryException {

        Screening screening = getScreening(1);
        List<Screening> screenings = List.of(screening);
        ScreeningDto screeningDto = getScreeningDto(1);
        Integer movieId = 1;
        when(screeningDao.findAllAvailableByMovieId(movieId))
                .thenReturn(screenings);
        when(screeningMapper.mapFrom(screening))
                .thenReturn(screeningDto);

        final List<ScreeningDto> actualResult = screeningService.findAllAvailable(movieId);

        assertThat(actualResult)
                .hasSize(1);
        actualResult.stream().map(ScreeningDto::getId)
                .forEach(id -> assertThat(id).isEqualTo(1));
    }

    @Test
    void testFindAllByFilter() throws RepositoryException {

        Movie movie = getMovie(1);
        List<Movie> movies = List.of(movie);
        MovieDto movieDto = getMovieDto(1);

        ScreeningFilterDto filter = getScreeningFilterDto();

        Map<String, Integer> mapOfAttributeAndNumber = new HashMap<>();
        mapOfAttributeAndNumber.put(TITLE, 1);
        mapOfAttributeAndNumber.put(GENRE, 2);
        mapOfAttributeAndNumber.put(DATE, 3);

        Map<String, Object> mapOfAttributeAndValue = new HashMap<>();
        mapOfAttributeAndValue.put(TITLE, filter.getTitle());
        mapOfAttributeAndValue.put(GENRE, filter.getGenre());
        mapOfAttributeAndValue.put(DATE, filter.getDate());

        String condition = " WHERE ( ";
        condition += " m." + TITLE + " LIKE concat(ltrim(?), '%') AND ";
        condition += " m." + GENRE + " LIKE ? AND ";
        condition += " DATE(start_time) = ?  AND ";
        condition += " 1 = 1)";

        when(screeningDao.findAllDistinctMoviesByFilter(condition, mapOfAttributeAndNumber, mapOfAttributeAndValue))
                .thenReturn(movies);

        when(movieMapper.mapFrom(movie))
                .thenReturn(movieDto);

        final List<MovieDto> actualResult = screeningService.findAllDistinctMoviesByFilter(filter);

        assertThat(actualResult)
                .hasSize(1);
        actualResult.stream().map(MovieDto::getId)
                .forEach(id -> assertThat(id).isEqualTo(1));
    }
}