package com.vasche.service;

import com.vasche.dto.filter.MovieFilterDto;
import com.vasche.dto.movie.CreateMovieDto;
import com.vasche.dto.movie.MovieDto;
import com.vasche.entity.Movie;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ServiceException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.movie.CreateMovieMapper;
import com.vasche.mapper.movie.MovieMapper;
import com.vasche.repository.MovieRepository;
import com.vasche.validator.CreateMovieValidator;
import com.vasche.validator.ValidationResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private CreateMovieMapper createMovieMapper;

    @Mock
    private MovieMapper movieMapper;

    @Mock
    private CreateMovieValidator createMovieValidator;

    @InjectMocks
    private MovieService movieService;


    @Test
    void testCreateIfValidationPassed() throws RepositoryException, ServiceException {

        CreateMovieDto createMovieDto = CreateMovieDto.builder().build();
        Movie movie = new Movie();
        movie.setId(1);
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        doReturn(validationResult).when(createMovieValidator).isValid(createMovieDto);

        when(validationResult.isValid())
                .thenReturn(true);
        doReturn(movie).when(createMovieMapper).mapFrom(createMovieDto);

        doReturn(movie).when(movieRepository).save(movie);

        Integer actualResult = movieService.create(createMovieDto);

        Assertions.assertThat(actualResult)
                .isEqualTo(1);
    }

    @Test
    void testCreateIfValidationFailed() {
        CreateMovieDto createMovieDto = CreateMovieDto.builder().build();
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
        CreateMovieDto createMovieDto = CreateMovieDto.builder().build();
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        when(createMovieValidator.isValid(createMovieDto))
                .thenReturn(validationResult);
        when(validationResult.isValid())
                .thenReturn(false);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> movieService.create(createMovieDto));
    }


    @Test
    public void testFindById() throws ServiceException, RepositoryException {
        MovieDto movieDto = MovieDto.builder().build();
        when(movieRepository.findById(1)).thenReturn(Optional.of(new Movie()));
        when(movieMapper.mapFrom(any(Movie.class))).thenReturn(movieDto);

        Optional<MovieDto> result = movieService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(result.get(), movieDto);
        verify(movieRepository, times(1)).findById(1);
        verify(movieMapper, times(1)).mapFrom(any(Movie.class));
    }

    @Test
    public void testFindAll() throws ServiceException, RepositoryException {
        when(movieRepository.findAll()).thenReturn(Arrays.asList(new Movie(), new Movie()));
        when(movieMapper.mapFrom(any(Movie.class))).thenReturn(MovieDto.builder().build());

        List<MovieDto> result = movieService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(movieRepository, times(1)).findAll();
        verify(movieMapper, times(2)).mapFrom(any(Movie.class));
    }

    @Test
    public void testFindAllByFilter() throws ServiceException, RepositoryException {
        MovieFilterDto filter = MovieFilterDto.builder().build();
        when(movieRepository.findAllByFilter(any(), any())).thenReturn(Arrays.asList(new Movie(), new Movie()));
        when(movieMapper.mapFrom(any(Movie.class))).thenReturn(MovieDto.builder().build());

        List<MovieDto> result = movieService.findAllByFilter(filter);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(movieRepository, times(1)).findAllByFilter(any(), any());
        verify(movieMapper, times(2)).mapFrom(any(Movie.class));
    }

    @Test
    public void testDeleteMovie() throws ServiceException, RepositoryException {
        when(movieRepository.delete(1)).thenReturn(true);

        boolean result = movieService.delete(1);

        assertTrue(result);
        verify(movieRepository, times(1)).delete(1);
    }

    @Test
    public void testCountNumberOfMovies() throws ServiceException, RepositoryException {
        when(movieRepository.findAll()).thenReturn(Arrays.asList(new Movie(), new Movie(), new Movie()));

        Integer count = movieService.countNumberOfMovies();

        assertEquals(3, count);
        verify(movieRepository, times(1)).findAll();
    }
}

