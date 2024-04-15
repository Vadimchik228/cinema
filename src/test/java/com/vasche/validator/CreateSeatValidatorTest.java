package com.vasche.validator;

import com.vasche.dao.LineDao;
import com.vasche.dto.seat.CreateSeatDto;
import com.vasche.entity.Line;
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

import static com.vasche.util.constants.ErrorCodes.INVALID_LINE_ID;
import static com.vasche.util.constants.ErrorCodes.INVALID_NUMBER;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class CreateSeatValidatorTest {


    @InjectMocks
    private CreateSeatValidator validator = CreateSeatValidator.getInstance();

    @Mock
    private LineDao lineDao = LineDao.getInstance();

    @Test
    void shouldPassValidation() throws DaoException {

        CreateSeatDto dto = CreateSeatDto.builder()
                .number("1")
                .lineId("1")
                .build();

        Line line = getLine();

        doReturn(Optional.of(line)).when(lineDao).findById(Integer.valueOf(dto.getLineId()));

        ValidationResult actualResult = validator.isValid(dto);

        assertTrue(actualResult.getErrors().isEmpty());
    }

    @Test
    void invalidNumber() throws DaoException {

        CreateSeatDto dto = CreateSeatDto.builder()
                .lineId("1")
                .build();

        Line line = getLine();

        doReturn(Optional.of(line)).when(lineDao).findById(Integer.valueOf(dto.getLineId()));

        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_NUMBER);
    }

    @Test
    void invalidLineId() throws DaoException {

        CreateSeatDto dto = CreateSeatDto.builder()
                .number("1")
                .lineId("1")
                .build();

        doReturn(Optional.empty()).when(lineDao).findById(Integer.valueOf(dto.getLineId()));

        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_LINE_ID);
    }

    @Test
    void invalidNumberLineId() throws DaoException {
        CreateSeatDto dto = CreateSeatDto.builder()
                .lineId("1")
                .build();

        doReturn(Optional.empty()).when(lineDao).findById(Integer.valueOf(dto.getLineId()));

        ValidationResult actualResult = validator.isValid(dto);

        Assertions.assertThat(actualResult.getErrors()).hasSize(2);
        List<String> errorCodes = actualResult.getErrors().stream()
                .map(Error::getCode)
                .toList();
        Assertions.assertThat(errorCodes).contains(INVALID_NUMBER, INVALID_LINE_ID);
    }

    private static Line getLine() {
        return Line.builder()
                .id(1)
                .number(1)
                .hallId(1)
                .build();
    }
}