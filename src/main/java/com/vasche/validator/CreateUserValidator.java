package com.vasche.validator;

import com.vasche.dao.UserDao;
import com.vasche.dto.user.CreateUserDto;
import com.vasche.entity.Role;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import static com.vasche.util.PersonalInfoUtil.*;
import static com.vasche.util.constants.ErrorCodes.*;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateUserValidator implements Validator<CreateUserDto> {

    private static final CreateUserValidator INSTANCE = new CreateUserValidator();

    private UserDao userDao = UserDao.getInstance();

    public static CreateUserValidator getInstance() {
        return INSTANCE;
    }

    @SneakyThrows
    @Override
    public ValidationResult isValid(CreateUserDto createUserDto) {
        var validationResult = new ValidationResult();

        if (!isCorrectFirstName(createUserDto.getFirstName())) {
            validationResult.add(Error.of(INVALID_FIRST_NAME, "First Name is invalid"));
        }
        if (!isCorrectLastName(createUserDto.getLastName())) {
            validationResult.add(Error.of(INVALID_LAST_NAME, "Last Name is invalid"));
        }
        if (!isCorrectEmail(createUserDto.getEmail())) {
            validationResult.add(Error.of(INVALID_EMAIL, "Email is invalid"));
        }
        if (userDao.findByEmail(createUserDto.getEmail()).isPresent()) {
            validationResult.add(Error.of(NOT_UNIQUE_EMAIL, "Email already exists"));
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
