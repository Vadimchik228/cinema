package com.vasche.service;

import com.vasche.repository.MovieRepository;
import com.vasche.dto.filter.MovieFilterDto;
import com.vasche.dto.movie.CreateMovieDto;
import com.vasche.dto.movie.MovieDto;
import com.vasche.entity.Movie;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.movie.CreateMovieMapper;
import com.vasche.mapper.movie.MovieMapper;
import com.vasche.validator.CreateMovieValidator;
import com.vasche.validator.ValidationResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.vasche.util.constants.FilteredAttributes.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest extends ServiceTestBase {

    @Mock
    private CreateMovieValidator createMovieValidator;
    @Mock
    private MovieRepository movieDao;
    @Mock
    private CreateMovieMapper createMovieMapper;
    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private MovieService movieService = new MovieService();

    @Test
    void testCreateIfValidationPassed() throws RepositoryException, IOException {

        CreateMovieDto createMovieDto = getCreateMovieDto();
        Movie movie = getMovie(1);
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        doReturn(validationResult).when(createMovieValidator).isValid(createMovieDto);

        when(validationResult.isValid())
                .thenReturn(true);
        doReturn(movie).when(createMovieMapper).mapFrom(createMovieDto);

        doReturn(movie).when(movieDao).save(movie);

        Integer actualResult = movieService.create(createMovieDto);

        Assertions.assertThat(actualResult)
                .isEqualTo(1);
    }

    @Test
    void testCreateIfValidationFailed() {
        CreateMovieDto createMovieDto = getCreateMovieDto();
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        when(createMovieValidator.isValid(createMovieDto))
                .thenReturn(validationResult);
        when(validationResult.isValid())
                .thenReturn(false);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> movieService.create(createMovieDto));
    }

    @Test
    void testUpdateIfValidationFailed() {
        CreateMovieDto createMovieDto = getCreateMovieDto();
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        when(createMovieValidator.isValid(createMovieDto))
                .thenReturn(validationResult);
        when(validationResult.isValid())
                .thenReturn(false);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> movieService.create(createMovieDto));
    }

    @Test
    void testDelete() throws RepositoryException {
        when(movieDao.delete(1))
                .thenReturn(true);

        boolean actualResult = movieService.delete(1);

        assertThat(actualResult).isTrue();
    }

    @Test
    void testFindByIdIfMovieExists() throws RepositoryException {
        Movie movie = getMovie(1);
        MovieDto movieDto = getMovieDto(1);
        when(movieDao.findById(movie.getId()))
                .thenReturn(Optional.of(movie));
        when(movieMapper.mapFrom(movie))
                .thenReturn(movieDto);

        final Optional<MovieDto> actualResult = movieService.findById(movie.getId());

        assertThat(actualResult)
                .isPresent();
        assertThat(actualResult.get())
                .isEqualTo(movieDto);
    }

    @Test
    void testFindByIdIfUserDoesNotExist() throws RepositoryException {
        when(movieDao.findById(1))
                .thenReturn(Optional.empty());

        final Optional<MovieDto> actualResult = movieService.findById(1);

        assertThat(actualResult)
                .isEmpty();
        verifyNoInteractions(movieMapper);
    }

    @Test
    void testFindAll() throws RepositoryException {

        Movie movie = getMovie(1);
        List<Movie> movies = List.of(movie);
        MovieDto movieDto = getMovieDto(1);
        when(movieDao.findAll())
                .thenReturn(movies);
        when(movieMapper.mapFrom(movie))
                .thenReturn(movieDto);

        final List<MovieDto> actualResult = movieService.findAll();

        assertThat(actualResult)
                .hasSize(1);
        actualResult.stream().map(MovieDto::getId)
                .forEach(id -> assertThat(id).isEqualTo(1));
    }

    @Test
    void testFindAllByFilter() throws RepositoryException {

        Movie movie = getMovie(1);
        List<Movie> movies = List.of(movie);
        MovieDto movieDto = getMovieDto(1);
        MovieFilterDto filter = getMovieFilterDto();

        Map<String, Integer> mapOfAttributeAndNumber = new HashMap<>();
        mapOfAttributeAndNumber.put(TITLE, 1);
        mapOfAttributeAndNumber.put(GENRE, 2);
        mapOfAttributeAndNumber.put(MINIMUM_AGE, 3);

        Map<String, Object> mapOfAttributeAndValue = new HashMap<>();
        mapOfAttributeAndValue.put(TITLE, filter.getTitle());
        mapOfAttributeAndValue.put(GENRE, filter.getGenre());
        mapOfAttributeAndValue.put(MINIMUM_AGE, filter.getMinimumAge());

        String condition = " WHERE ( ";
        condition += TITLE.concat(" LIKE concat(ltrim(?), '%') AND ");
        condition += GENRE.concat(" LIKE ? AND ");
        condition += MINIMUM_AGE.concat("  = ?  AND ");
        condition += " 1 = 1)";

        when(movieDao.findAllByFilter(condition, mapOfAttributeAndNumber, mapOfAttributeAndValue))
                .thenReturn(movies);

        when(movieMapper.mapFrom(movie))
                .thenReturn(movieDto);

        final List<MovieDto> actualResult = movieService.findAllByFilter(filter);

        assertThat(actualResult)
                .hasSize(1);
        actualResult.stream().map(MovieDto::getId)
                .forEach(id -> assertThat(id).isEqualTo(1));
    }
}
