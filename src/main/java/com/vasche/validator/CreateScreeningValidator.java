package com.vasche.validator;

import com.vasche.dto.screening.CreateScreeningDto;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ValidatorException;
import com.vasche.repository.HallRepository;
import com.vasche.repository.MovieRepository;
import com.vasche.util.LocalDateTimeFormatter;
import com.vasche.util.NumericUtil;
import org.apache.log4j.Logger;

import static com.vasche.util.constants.ErrorCodes.*;

public class CreateScreeningValidator implements Validator<CreateScreeningDto> {
    private static final Logger LOGGER = Logger.getLogger(CreateScreeningValidator.class);
    private final HallRepository hallRepository;
    private final MovieRepository movieRepository;

    public CreateScreeningValidator() {
        hallRepository = new HallRepository();
        movieRepository = new MovieRepository();
    }

    @Override
    public ValidationResult isValid(final CreateScreeningDto createScreeningDto) {
        try {
            final ValidationResult validationResult = new ValidationResult();

            if (!LocalDateTimeFormatter.isValid(createScreeningDto.getStartTime())) {
                validationResult.add(Error.of(INVALID_START_TIME, "Start time is invalid"));
            }
            if (!NumericUtil.isPrice(createScreeningDto.getPrice())) {
                validationResult.add(Error.of(INVALID_PRICE, "Price is invalid"));
            }
            if (hallRepository.findById(Integer.valueOf(createScreeningDto.getHallId())).isEmpty()) {
                validationResult.add(Error.of(INVALID_HALL_ID, "HallId is invalid"));
            }
            if (movieRepository.findById(Integer.valueOf(createScreeningDto.getMovieId())).isEmpty()) {
                validationResult.add(Error.of(INVALID_MOVIE_ID, "MovieId is invalid"));
            }

            return validationResult;
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ValidatorException("Couldn't verify if createScreeningDto is valid", e);
        }
    }
}
