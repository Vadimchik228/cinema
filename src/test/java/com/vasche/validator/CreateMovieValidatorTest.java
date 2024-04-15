package com.vasche.validator;

import com.vasche.dto.movie.CreateMovieDto;
import com.vasche.entity.Genre;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.vasche.util.constants.ErrorCodes.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateMovieValidatorTest {
    private final CreateMovieValidator validator = CreateMovieValidator.getInstance();

    @Test
    void shouldPassValidation() {
        CreateMovieDto dto = CreateMovieDto.builder()
                .title("Home alone")
                .minimumAge("6")
                .description("This is an American film")
                .durationMin("200")
                .genre(Genre.COMEDY.name())
                .build();

        ValidationResult actualResult = validator.isValid(dto);

        assertTrue(actualResult.getErrors().isEmpty());
    }

    @Test
    void invalidTitle() {
        CreateMovieDto dto = CreateMovieDto.builder()
                .minimumAge("6")
                .description("This is an American film")
                .durationMin("200")
                .genre(Genre.COMEDY.name())
                .build();

        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(NULL_TITLE);
    }

    @Test
    void invalidGenre() {
        CreateMovieDto dto = CreateMovieDto.builder()
                .title("Home alone")
                .minimumAge("6")
                .description("This is an American film")
                .durationMin("200")
                .genre("fake-genre")
                .build();

        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(NULL_GENRE);
    }

    @Test
    void invalidMinimumAge() {
        CreateMovieDto dto = CreateMovieDto.builder()
                .title("Home alone")
                .minimumAge("32")
                .description("This is an American film")
                .durationMin("200")
                .genre(Genre.COMEDY.name())
                .build();

        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_MINIMUM_AGE);
    }

    @Test
    void invalidDurationMin() {
        CreateMovieDto dto = CreateMovieDto.builder()
                .title("Home alone")
                .minimumAge("6")
                .description("This is an American film")
                .durationMin("-100")
                .genre(Genre.COMEDY.name())
                .build();

        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_DURATION_MIN);
    }

    @Test
    void invalidTitleGenreMinimumAgeDurationMin() {
        CreateMovieDto dto = CreateMovieDto.builder()
                .minimumAge("22")
                .description("This is an American film")
                .durationMin("-100")
                .genre("fake-genre")
                .build();

        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).hasSize(4);
        List<String> errorCodes = actualResult.getErrors().stream()
                .map(Error::getCode)
                .toList();
        assertThat(errorCodes).contains(NULL_TITLE, NULL_GENRE, INVALID_MINIMUM_AGE, INVALID_DURATION_MIN);
    }
}
