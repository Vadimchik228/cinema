package com.vasche.validator;

import com.vasche.dao.HallDao;
import com.vasche.dao.MovieDao;
import com.vasche.dto.screening.CreateScreeningDto;
import com.vasche.util.LocalDateTimeFormatter;
import com.vasche.util.NumericUtil;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import static com.vasche.util.constants.ErrorCodes.*;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateScreeningValidator implements Validator<CreateScreeningDto> {

    private static final CreateScreeningValidator INSTANCE = new CreateScreeningValidator();

    private HallDao hallDao = HallDao.getInstance();
    private MovieDao movieDao = MovieDao.getInstance();

    public static CreateScreeningValidator getInstance() {
        return INSTANCE;
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
        if (hallDao.findById(Integer.valueOf(createScreeningDto.getHallId())).isEmpty()) {
            validationResult.add(Error.of(INVALID_HALL_ID, "Hall Id in invalid"));
        }
        if (movieDao.findById(Integer.valueOf(createScreeningDto.getMovieId())).isEmpty()) {
            validationResult.add(Error.of(INVALID_MOVIE_ID, "Movie Id in invalid"));
        }

        return validationResult;
    }
}
