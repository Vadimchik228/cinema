package com.vasche.validator;

import com.vasche.repository.HallRepository;
import com.vasche.dto.hall.CreateHallDto;
import lombok.SneakyThrows;

import static com.vasche.util.constants.ErrorCodes.NOT_UNIQUE_NAME;
import static com.vasche.util.constants.ErrorCodes.NULL_NAME;

public class CreateHallValidator implements Validator<CreateHallDto> {

    private HallRepository hallRepository;

    public CreateHallValidator() {
        hallRepository = new HallRepository();
    }

    @SneakyThrows
    @Override
    public ValidationResult isValid(final CreateHallDto createHallDto) {
        final ValidationResult validationResult = new ValidationResult();

        if (createHallDto.getName() == null) {
            validationResult.add(Error.of(NULL_NAME, "Name is null"));
        }
        if (hallRepository.findByName(createHallDto.getName()).isPresent()) {
            validationResult.add(Error.of(NOT_UNIQUE_NAME, "Such Name already exists"));
        }

        return validationResult;
    }

}