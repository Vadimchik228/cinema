package com.vasche.validator;

import com.vasche.dto.screening.CreateScreeningDto;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.vasche.util.constants.ErrorCodes.INVALID_PRICE;
import static com.vasche.util.constants.ErrorCodes.INVALID_START_TIME;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class CreateScreeningValidatorTest {

    private final CreateScreeningValidator validator = new CreateScreeningValidator();


    @Test
    void shouldPassValidation() {

        CreateScreeningDto dto = CreateScreeningDto.builder()
                .movieId("1")
                .hallId("1")
                .startTime("2024-01-01 16:30")
                .price("1000")
                .build();


        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).isEmpty();
    }

    @Test
    void invalidPrice() {

        CreateScreeningDto dto = CreateScreeningDto.builder()
                .movieId("1")
                .hallId("1")
                .startTime("2024-01-01 16:30")
                .build();

        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_PRICE);
    }

    @Test
    void invalidStartTime() {

        CreateScreeningDto dto = CreateScreeningDto.builder()
                .movieId("1")
                .hallId("1")
                .price("1000")
                .build();

        ValidationResult actualResult = validator.isValid(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_START_TIME);
    }

    @Test
    void invalidPriceAndStartTime() {
        CreateScreeningDto dto = CreateScreeningDto.builder()
                .movieId("1")
                .hallId("1")
                .build();

        ValidationResult actualResult = validator.isValid(dto);

        Assertions.assertThat(actualResult.getErrors()).hasSize(2);
        List<String> errorCodes = actualResult.getErrors().stream()
                .map(Error::getCode)
                .toList();
        Assertions.assertThat(errorCodes).contains(INVALID_PRICE, INVALID_START_TIME);
    }

}
