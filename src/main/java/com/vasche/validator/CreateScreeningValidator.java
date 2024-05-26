package com.vasche.validator;

import com.vasche.dto.screening.CreateScreeningDto;
import com.vasche.util.LocalDateTimeFormatter;
import com.vasche.util.NumericUtil;

import static com.vasche.util.constants.ErrorCodes.INVALID_PRICE;
import static com.vasche.util.constants.ErrorCodes.INVALID_START_TIME;

public class CreateScreeningValidator implements Validator<CreateScreeningDto> {


    public CreateScreeningValidator() {

    }

    @Override
    public ValidationResult isValid(final CreateScreeningDto createScreeningDto) {
        final ValidationResult validationResult = new ValidationResult();

        if (!LocalDateTimeFormatter.isValid(createScreeningDto.getStartTime())) {
            validationResult.add(Error.of(INVALID_START_TIME, "Start time is invalid"));
        }
        if (!NumericUtil.isPrice(createScreeningDto.getPrice())) {
            validationResult.add(Error.of(INVALID_PRICE, "Price is invalid"));
        }

        return validationResult;
    }
}
