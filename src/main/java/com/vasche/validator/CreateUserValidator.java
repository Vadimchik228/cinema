package com.vasche.validator;

import com.vasche.dto.user.CreateUserDto;
import com.vasche.entity.Role;

import static com.vasche.util.PersonalInfoUtil.*;
import static com.vasche.util.constants.ErrorCodes.*;

public class CreateUserValidator implements Validator<CreateUserDto> {


    public CreateUserValidator() {
    }

    @Override
    public ValidationResult isValid(CreateUserDto createUserDto) {
        var validationResult = new ValidationResult();

        if (!isCorrectName(createUserDto.getFirstName())) {
            validationResult.add(Error.of(INVALID_FIRST_NAME, "First name is invalid"));
        }
        if (!isCorrectName(createUserDto.getLastName())) {
            validationResult.add(Error.of(INVALID_LAST_NAME, "Last name is invalid"));
        }
        if (!isCorrectEmail(createUserDto.getEmail())) {
            validationResult.add(Error.of(INVALID_EMAIL, "Email is invalid"));
        }
        if (!isCorrectPassword(createUserDto.getPassword())) {
            validationResult.add(Error.of(INVALID_PASSWORD, "Password is invalid"));
        }
        if (Role.find(createUserDto.getRole()).isEmpty()) {
            validationResult.add(Error.of(INVALID_ROLE, "Role is invalid"));
        }

        return validationResult;
    }

}
