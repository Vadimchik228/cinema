package com.vasche.validator;

import com.vasche.dao.LineDao;
import com.vasche.dto.seat.CreateSeatDto;
import com.vasche.util.NumericUtil;
import com.vasche.util.constants.ErrorCodes;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import static com.vasche.util.constants.ErrorCodes.*;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateSeatValidator implements Validator<CreateSeatDto> {

    private static final CreateSeatValidator INSTANCE = new CreateSeatValidator();
    private LineDao lineDao = LineDao.getInstance();

    public static CreateSeatValidator getInstance() {
        return INSTANCE;
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
