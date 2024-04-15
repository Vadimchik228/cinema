package com.vasche.validator;

import com.vasche.dao.HallDao;
import com.vasche.dto.line.CreateLineDto;
import com.vasche.entity.Hall;
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

import static com.vasche.util.constants.ErrorCodes.INVALID_HALL_ID;
import static com.vasche.util.constants.ErrorCodes.INVALID_NUMBER;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class CreateLineValidatorTest {


    @InjectMocks
    private CreateLineValidator validator = CreateLineValidator.getInstance();

    @Mock
    private HallDao hallDao = HallDao.getInstance();

    @Test
    void shouldPassValidation() throws DaoException {

        CreateLineDto dto = CreateLineDto.builder()
                .number("1")
                .hallId("1")
                .build();

        Hall hall = getHall();

        doReturn(Optional.of(hall)).when(hallDao).findById(Integer.valueOf(dto.getHallId()));

        ValidationResult actualResult = validator.isValid(dto);

        assertTrue(actualResult.getErrors().isEmpty());
    }

    @Test
    void invalidNumber() throws DaoException {

        CreateLineDto dto = CreateLineDto.builder()
                .hallId("1")
                .build();

        Hall hall = getHall();

        doReturn(Optional.of(hall)).when(hallDao).findById(Integer.valueOf(dto.getHallId()));

        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_NUMBER);
    }

    @Test
    void invalidHallId() throws DaoException {

        CreateLineDto dto = CreateLineDto.builder()
                .number("1")
                .hallId("1")
                .build();

        doReturn(Optional.empty()).when(hallDao).findById(Integer.valueOf(dto.getHallId()));

        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_HALL_ID);
    }

    @Test
    void invalidNumberHallId() throws DaoException {
        CreateLineDto dto = CreateLineDto.builder()
                .number("TEXT")
                .hallId("1")
                .build();

        doReturn(Optional.empty()).when(hallDao).findById(Integer.valueOf(dto.getHallId()));

        ValidationResult actualResult = validator.isValid(dto);

        Assertions.assertThat(actualResult.getErrors()).hasSize(2);
        List<String> errorCodes = actualResult.getErrors().stream()
                .map(Error::getCode)
                .toList();
        Assertions.assertThat(errorCodes).contains(INVALID_NUMBER, INVALID_HALL_ID);
    }

    private static Hall getHall() {
        return Hall.builder()
                .id(1)
                .name("Hall")
                .build();
    }
}
