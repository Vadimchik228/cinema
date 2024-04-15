package com.vasche.validator;

import com.vasche.dao.HallDao;
import com.vasche.dao.MovieDao;
import com.vasche.dto.screening.CreateScreeningDto;
import com.vasche.entity.Genre;
import com.vasche.entity.Hall;
import com.vasche.entity.Movie;
import com.vasche.exception.DaoException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.vasche.util.constants.ErrorCodes.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class CreateScreeningValidatorTest {
    @InjectMocks
    private CreateScreeningValidator validator = CreateScreeningValidator.getInstance();
    @Mock
    private HallDao hallDao = HallDao.getInstance();
    @Mock
    private MovieDao movieDao = MovieDao.getInstance();

    @Test
    void shouldPassValidation() throws DaoException {

        CreateScreeningDto dto = CreateScreeningDto.builder()
                .movieId("1")
                .hallId("1")
                .startTime("2024-01-01 16:30")
                .price("1000")
                .build();

        var movie = getMovie();
        var hall = getHall();

        doReturn(Optional.of(movie)).when(movieDao).findById(Integer.valueOf(dto.getMovieId()));
        doReturn(Optional.of(hall)).when(hallDao).findById(Integer.valueOf(dto.getHallId()));

        ValidationResult actualResult = validator.isValid(dto);

        assertTrue(actualResult.getErrors().isEmpty());
    }

    @Test
    void invalidPrice() throws DaoException {

        CreateScreeningDto dto = CreateScreeningDto.builder()
                .movieId("1")
                .hallId("1")
                .startTime("2024-01-01 16:30")
                .build();

        var movie = getMovie();
        var hall = getHall();

        doReturn(Optional.of(movie)).when(movieDao).findById(Integer.valueOf(dto.getMovieId()));
        doReturn(Optional.of(hall)).when(hallDao).findById(Integer.valueOf(dto.getHallId()));

        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_PRICE);
    }

    @Test
    void invalidStartTime() throws DaoException {

        CreateScreeningDto dto = CreateScreeningDto.builder()
                .movieId("1")
                .hallId("1")
                .price("1000")
                .build();

        var movie = getMovie();
        var hall = getHall();

        doReturn(Optional.of(movie)).when(movieDao).findById(Integer.valueOf(dto.getMovieId()));
        doReturn(Optional.of(hall)).when(hallDao).findById(Integer.valueOf(dto.getHallId()));

        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_START_TIME);
    }

    @Test
    void invalidHallId() throws DaoException {

        CreateScreeningDto dto = CreateScreeningDto.builder()
                .movieId("1")
                .hallId("1")
                .startTime("2024-01-01 16:30")
                .price("1000")
                .build();

        var movie = getMovie();

        doReturn(Optional.of(movie)).when(movieDao).findById(Integer.valueOf(dto.getMovieId()));
        doReturn(Optional.empty()).when(hallDao).findById(Integer.valueOf(dto.getHallId()));

        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_HALL_ID);
    }

    @Test
    void invalidMovieId() throws DaoException {

        CreateScreeningDto dto = CreateScreeningDto.builder()
                .movieId("1")
                .hallId("1")
                .startTime("2024-01-01 16:30")
                .price("1000")
                .build();

        var hall = getHall();

        doReturn(Optional.empty()).when(movieDao).findById(Integer.valueOf(dto.getMovieId()));
        doReturn(Optional.of(hall)).when(hallDao).findById(Integer.valueOf(dto.getHallId()));

        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_MOVIE_ID);
    }

    @Test
    void invalidPriceStartTimeHallIdMovieId() throws DaoException {
        CreateScreeningDto dto = CreateScreeningDto.builder()
                .movieId("1")
                .hallId("1")
                .build();

        doReturn(Optional.empty()).when(movieDao).findById(Integer.valueOf(dto.getMovieId()));
        doReturn(Optional.empty()).when(hallDao).findById(Integer.valueOf(dto.getHallId()));

        ValidationResult actualResult = validator.isValid(dto);

        Assertions.assertThat(actualResult.getErrors()).hasSize(4);
        List<String> errorCodes = actualResult.getErrors().stream()
                .map(Error::getCode)
                .toList();
        Assertions.assertThat(errorCodes).contains(INVALID_PRICE, INVALID_START_TIME, INVALID_HALL_ID, INVALID_MOVIE_ID);
    }

    private static Movie getMovie() {
        return Movie.builder()
                .id(1)
                .title("1+1")
                .genre(Genre.DRAMA)
                .durationMin(150)
                .minimumAge(16)
                .description("Some description")
                .build();
    }

    private static Hall getHall() {
        return Hall.builder()
                .name("Hall 1")
                .id(1)
                .build();
    }
}
