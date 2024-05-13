package com.vasche.validator;

import com.vasche.dto.reservation.CreateReservationDto;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ValidatorException;
import com.vasche.repository.HallRepository;
import com.vasche.repository.ScreeningRepository;
import com.vasche.repository.SeatRepository;
import com.vasche.repository.UserRepository;
import org.apache.log4j.Logger;

import static com.vasche.util.constants.ErrorCodes.*;

public class CreateReservationValidator implements Validator<CreateReservationDto> {
    private static final Logger LOGGER = Logger.getLogger(CreateReservationValidator.class);
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;

    public CreateReservationValidator() {
        screeningRepository = new ScreeningRepository();
        seatRepository = new SeatRepository();
        userRepository = new UserRepository();
    }

    @Override
    public ValidationResult isValid(final CreateReservationDto createReservationDto) {
        try {
            final ValidationResult validationResult = new ValidationResult();
            if (seatRepository.findById(Integer.valueOf(createReservationDto.getSeatId())).isEmpty()) {
                validationResult.add(Error.of(INVALID_SEAT_ID, "SeatId is invalid"));
            }
            if (userRepository.findById(Integer.valueOf(createReservationDto.getUserId())).isEmpty()) {
                validationResult.add(Error.of(INVALID_USER_ID, "UserId is invalid"));
            }
            if (screeningRepository.findById(Integer.valueOf(createReservationDto.getScreeningId())).isEmpty()) {
                validationResult.add(Error.of(INVALID_SCREENING_ID, "ScreeningId is invalid"));
            }
            return validationResult;
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ValidatorException("Couldn't verify if createReservationDto is valid", e);
        }
    }
}
