package com.vasche.validator;

import com.vasche.dao.HallDao;
import com.vasche.dto.hall.CreateHallDto;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import static com.vasche.util.constants.ErrorCodes.NOT_UNIQUE_NAME;
import static com.vasche.util.constants.ErrorCodes.NULL_NAME;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateHallValidator implements Validator<CreateHallDto> {
    private static final CreateHallValidator INSTANCE = new CreateHallValidator();
    private HallDao hallDao = HallDao.getInstance();

    public static CreateHallValidator getInstance() {
        return INSTANCE;
    }

    @SneakyThrows
    @Override
    public ValidationResult isValid(final CreateHallDto createHallDto) {
        final ValidationResult validationResult = new ValidationResult();

        if (createHallDto.getName() == null) {
            validationResult.add(Error.of(NULL_NAME, "Name is null"));
        }
        if (hallDao.findByName(createHallDto.getName()).isPresent()) {
            validationResult.add(Error.of(NOT_UNIQUE_NAME, "Such Name already exists"));
        }

        return validationResult;
    }

}
