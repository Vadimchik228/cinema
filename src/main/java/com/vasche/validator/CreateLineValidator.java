package com.vasche.validator;

import com.vasche.repository.HallRepository;
import com.vasche.dto.line.CreateLineDto;
import com.vasche.util.NumericUtil;
import lombok.SneakyThrows;

import static com.vasche.util.constants.ErrorCodes.INVALID_HALL_ID;
import static com.vasche.util.constants.ErrorCodes.INVALID_NUMBER;

public class CreateLineValidator implements Validator<CreateLineDto> {

    private HallRepository hallRepository;

    public CreateLineValidator() {
        hallRepository = new HallRepository();
    }

    @SneakyThrows
    @Override
    public ValidationResult isValid(final CreateLineDto createLineDto) {
        final ValidationResult validationResult = new ValidationResult();

        if (!NumericUtil.isPositiveNumber(createLineDto.getNumber())) {
            validationResult.add(Error.of(INVALID_NUMBER, "Number is invalid"));
        }
        if (hallRepository.findById(Integer.valueOf(createLineDto.getHallId())).isEmpty()) {
            validationResult.add(Error.of(INVALID_HALL_ID, "Hall Id in invalid"));
        }

        return validationResult;
    }
}
