package com.vasche.validator;

import com.vasche.dto.movie.CreateMovieDto;
import com.vasche.entity.Genre;
import com.vasche.util.NumericUtil;

import static com.vasche.util.constants.ErrorCodes.*;

public class CreateMovieValidator implements Validator<CreateMovieDto> {

    public CreateMovieValidator() {
    }

    @Override
    public ValidationResult isValid(final CreateMovieDto createMovieDto) {
        final ValidationResult validationResult = new ValidationResult();

        if (Genre.find(createMovieDto.getGenre()).isEmpty()) {
            validationResult.add(Error.of(NULL_GENRE, "Genre is null"));
        }
        if (createMovieDto.getTitle() == null) {
            validationResult.add(Error.of(NULL_TITLE, "Title is null"));
        }
        if (!NumericUtil.isPositiveNumber(createMovieDto.getDurationMin())) {
            validationResult.add(Error.of(INVALID_DURATION_MIN, "Duration In Minutes is invalid"));
        }
        if (!NumericUtil.isMinimumAge(createMovieDto.getMinimumAge())) {
            validationResult.add(Error.of(INVALID_MINIMUM_AGE, "Minimum Age is invalid"));
        }

        return validationResult;
    }
}
