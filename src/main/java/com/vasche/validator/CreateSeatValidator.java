package com.vasche.validator;

import com.vasche.repository.LineRepository;
import com.vasche.dto.seat.CreateSeatDto;
import com.vasche.util.NumericUtil;
import lombok.SneakyThrows;

import static com.vasche.util.constants.ErrorCodes.INVALID_LINE_ID;
import static com.vasche.util.constants.ErrorCodes.INVALID_NUMBER;

public class CreateSeatValidator implements Validator<CreateSeatDto> {

    private final LineRepository lineDao;

    public CreateSeatValidator() {
        lineDao = new LineRepository();
    }

    @SneakyThrows
    @Override
    public ValidationResult isValid(final CreateSeatDto createSeatDto) {
        final ValidationResult validationResult = new ValidationResult();

        if (!NumericUtil.isPositiveNumber(createSeatDto.getNumber())) {
            validationResult.add(Error.of(INVALID_NUMBER, "Number is invalid"));
        }
        if (lineDao.findById(Integer.valueOf(createSeatDto.getLineId())).isEmpty()) {
            validationResult.add(Error.of(INVALID_LINE_ID, "Line Id is invalid"));
        }

        return validationResult;
    }

}
