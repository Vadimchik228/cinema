package com.vasche.validator;

import com.vasche.repository.HallRepository;
import com.vasche.repository.MovieRepository;
import com.vasche.dto.screening.CreateScreeningDto;
import com.vasche.util.LocalDateTimeFormatter;
import com.vasche.util.NumericUtil;
import lombok.SneakyThrows;

import static com.vasche.util.constants.ErrorCodes.*;

public class CreateScreeningValidator implements Validator<CreateScreeningDto> {

    private final HallRepository hallRepository;
    private final MovieRepository movieRepository;

    public CreateScreeningValidator() {
        hallRepository = new HallRepository();
        movieRepository = new MovieRepository();
    }

    @SneakyThrows
    @Override
    public ValidationResult isValid(final CreateScreeningDto createScreeningDto) {
        final ValidationResult validationResult = new ValidationResult();

        if (!LocalDateTimeFormatter.isValid(createScreeningDto.getStartTime())) {
            validationResult.add(Error.of(INVALID_START_TIME, "Start time is invalid"));
        }
        if (!NumericUtil.isPrice(createScreeningDto.getPrice())) {
            validationResult.add(Error.of(INVALID_PRICE, "Price is invalid"));
        }
        if (hallRepository.findById(Integer.valueOf(createScreeningDto.getHallId())).isEmpty()) {
            validationResult.add(Error.of(INVALID_HALL_ID, "Hall Id in invalid"));
        }
        if (movieRepository.findById(Integer.valueOf(createScreeningDto.getMovieId())).isEmpty()) {
            validationResult.add(Error.of(INVALID_MOVIE_ID, "Movie Id in invalid"));
        }

        return validationResult;
    }
}
