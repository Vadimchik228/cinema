package com.vasche.validator;

import com.vasche.dao.ScreeningDao;
import com.vasche.dao.SeatDao;
import com.vasche.dao.UserDao;
import com.vasche.dto.reservation.CreateReservationDto;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import static com.vasche.util.constants.ErrorCodes.*;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateReservationValidator implements Validator<CreateReservationDto> {

    private static final CreateReservationValidator INSTANCE = new CreateReservationValidator();

    private ScreeningDao screeningDao = ScreeningDao.getInstance();
    private SeatDao seatDao = SeatDao.getInstance();
    private UserDao userDao = UserDao.getInstance();


    public static CreateReservationValidator getInstance() {
        return INSTANCE;
    }


    @SneakyThrows
    @Override
    public ValidationResult isValid(final CreateReservationDto createReservationDto) {
        final ValidationResult validationResult = new ValidationResult();

        if (seatDao.findById(Integer.valueOf(createReservationDto.getSeatId())).isEmpty()) {
            validationResult.add(Error.of(INVALID_SEAT_ID, "Seat Id in invalid"));
        }
        if (userDao.findById(Integer.valueOf(createReservationDto.getUserId())).isEmpty()) {
            validationResult.add(Error.of(INVALID_USER_ID, "User Id in invalid"));
        }
        if (screeningDao.findById(Integer.valueOf(createReservationDto.getScreeningId())).isEmpty()) {
            validationResult.add(Error.of(INVALID_SCREENING_ID, "Screening Id in invalid"));
        }

        return validationResult;
    }
}
