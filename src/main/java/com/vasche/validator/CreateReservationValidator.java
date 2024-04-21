package com.vasche.validator;

import com.vasche.repository.ScreeningRepository;
import com.vasche.repository.SeatRepository;
import com.vasche.repository.UserRepository;
import com.vasche.dto.reservation.CreateReservationDto;
import lombok.SneakyThrows;

import static com.vasche.util.constants.ErrorCodes.*;

public class CreateReservationValidator implements Validator<CreateReservationDto> {

    private ScreeningRepository screeningRepository;
    private SeatRepository seatRepository;
    private UserRepository userRepository;

    public CreateReservationValidator() {
        screeningRepository = new ScreeningRepository();
        seatRepository = new SeatRepository();
        userRepository = new UserRepository();
    }

    @SneakyThrows
    @Override
    public ValidationResult isValid(final CreateReservationDto createReservationDto) {
        final ValidationResult validationResult = new ValidationResult();

        if (seatRepository.findById(Integer.valueOf(createReservationDto.getSeatId())).isEmpty()) {
            validationResult.add(Error.of(INVALID_SEAT_ID, "Seat Id in invalid"));
        }
        if (userRepository.findById(Integer.valueOf(createReservationDto.getUserId())).isEmpty()) {
            validationResult.add(Error.of(INVALID_USER_ID, "User Id in invalid"));
        }
        if (screeningRepository.findById(Integer.valueOf(createReservationDto.getScreeningId())).isEmpty()) {
            validationResult.add(Error.of(INVALID_SCREENING_ID, "Screening Id in invalid"));
        }

        return validationResult;
    }
}
