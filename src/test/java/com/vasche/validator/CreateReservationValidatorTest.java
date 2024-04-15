package com.vasche.validator;

import com.vasche.dao.ScreeningDao;
import com.vasche.dao.SeatDao;
import com.vasche.dao.UserDao;
import com.vasche.dto.reservation.CreateReservationDto;
import com.vasche.entity.Role;
import com.vasche.entity.Screening;
import com.vasche.entity.Seat;
import com.vasche.entity.User;
import com.vasche.exception.DaoException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.vasche.util.constants.ErrorCodes.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class CreateReservationValidatorTest {
    @InjectMocks
    private CreateReservationValidator validator = CreateReservationValidator.getInstance();
    @Mock
    private UserDao userDao = UserDao.getInstance();
    @Mock
    private ScreeningDao screeningDao = ScreeningDao.getInstance();
    @Mock
    private SeatDao seatDao = SeatDao.getInstance();

    @Test
    void shouldPassValidation() throws DaoException {

        CreateReservationDto dto = CreateReservationDto.builder()
                .userId("1")
                .seatId("1")
                .screeningId("1")
                .build();

        var seat = getSeat();
        var user = getUser();
        var screening = getScreening();

        doReturn(Optional.of(seat)).when(seatDao).findById(Integer.valueOf(dto.getSeatId()));
        doReturn(Optional.of(user)).when(userDao).findById(Integer.valueOf(dto.getUserId()));
        doReturn(Optional.of(screening)).when(screeningDao).findById(Integer.valueOf(dto.getScreeningId()));

        ValidationResult actualResult = validator.isValid(dto);

        assertTrue(actualResult.getErrors().isEmpty());
    }

    @Test
    void invalidSeatId() throws DaoException {

        CreateReservationDto dto = CreateReservationDto.builder()
                .userId("1")
                .seatId("1")
                .screeningId("1")
                .build();

        var user = getUser();
        var screening = getScreening();

        doReturn(Optional.empty()).when(seatDao).findById(Integer.valueOf(dto.getSeatId()));
        doReturn(Optional.of(user)).when(userDao).findById(Integer.valueOf(dto.getUserId()));
        doReturn(Optional.of(screening)).when(screeningDao).findById(Integer.valueOf(dto.getScreeningId()));

        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_SEAT_ID);
    }

    @Test
    void invalidUserId() throws DaoException {

        CreateReservationDto dto = CreateReservationDto.builder()
                .userId("1")
                .seatId("1")
                .screeningId("1")
                .build();

        var seat = getSeat();
        var screening = getScreening();

        doReturn(Optional.of(seat)).when(seatDao).findById(Integer.valueOf(dto.getSeatId()));
        doReturn(Optional.empty()).when(userDao).findById(Integer.valueOf(dto.getUserId()));
        doReturn(Optional.of(screening)).when(screeningDao).findById(Integer.valueOf(dto.getScreeningId()));

        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_USER_ID);
    }

    @Test
    void invalidScreeningId() throws DaoException {

        CreateReservationDto dto = CreateReservationDto.builder()
                .userId("1")
                .seatId("1")
                .screeningId("1")
                .build();

        var seat = getSeat();
        var user = getUser();

        doReturn(Optional.of(seat)).when(seatDao).findById(Integer.valueOf(dto.getSeatId()));
        doReturn(Optional.of(user)).when(userDao).findById(Integer.valueOf(dto.getUserId()));
        doReturn(Optional.empty()).when(screeningDao).findById(Integer.valueOf(dto.getScreeningId()));

        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_SCREENING_ID);
    }


    @Test
    void invalidSeatIdUserIdScreeningId() throws DaoException {
        CreateReservationDto dto = CreateReservationDto.builder()
                .userId("1")
                .seatId("1")
                .screeningId("1")
                .build();

        doReturn(Optional.empty()).when(seatDao).findById(Integer.valueOf(dto.getSeatId()));
        doReturn(Optional.empty()).when(userDao).findById(Integer.valueOf(dto.getUserId()));
        doReturn(Optional.empty()).when(screeningDao).findById(Integer.valueOf(dto.getScreeningId()));

        ValidationResult actualResult = validator.isValid(dto);

        Assertions.assertThat(actualResult.getErrors()).hasSize(3);
        List<String> errorCodes = actualResult.getErrors().stream()
                .map(Error::getCode)
                .toList();
        Assertions.assertThat(errorCodes).contains(INVALID_SEAT_ID, INVALID_USER_ID, INVALID_SCREENING_ID);
    }

    private static Seat getSeat() {
        return Seat.builder()
                .lineId(1)
                .number(1)
                .build();
    }

    private static Screening getScreening() {
        return Screening.builder()
                .id(1)
                .movieId(1)
                .hallId(1)
                .startTime(LocalDateTime.of(2024, 1, 1, 12, 30))
                .price(BigDecimal.valueOf(1000))
                .build();
    }

    private static User getUser() {
        return User.builder()
                .id(1)
                .firstName("Vadim")
                .lastName("Scheb")
                .role(Role.CLIENT)
                .email("Schebetovskiy@gmail.com")
                .password("Vadim123!@#")
                .build();
    }
}
