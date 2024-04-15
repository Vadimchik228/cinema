package com.vasche.validator;

import com.vasche.dao.HallDao;
import com.vasche.dto.line.CreateLineDto;
import com.vasche.util.NumericUtil;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import static com.vasche.util.constants.ErrorCodes.INVALID_HALL_ID;
import static com.vasche.util.constants.ErrorCodes.INVALID_NUMBER;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateLineValidator implements Validator<CreateLineDto> {

    private static final CreateLineValidator INSTANCE = new CreateLineValidator();
    private HallDao hallDao = HallDao.getInstance();

    public static CreateLineValidator getInstance() {
        return INSTANCE;
    }


    @SneakyThrows
    @Override
    public ValidationResult isValid(final CreateLineDto createLineDto) {
        final ValidationResult validationResult = new ValidationResult();

        if (!NumericUtil.isPositiveNumber(createLineDto.getNumber())) {
            validationResult.add(Error.of(INVALID_NUMBER, "Number is invalid"));
        }
        if (hallDao.findById(Integer.valueOf(createLineDto.getHallId())).isEmpty()) {
            validationResult.add(Error.of(INVALID_HALL_ID, "Hall Id in invalid"));
        }

        return validationResult;
    }
}
