package com.vasche.validator;

import com.vasche.dto.user.CreateUserDto;
import com.vasche.entity.Role;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ValidatorException;
import com.vasche.repository.UserRepository;
import org.apache.log4j.Logger;

import static com.vasche.util.PersonalInfoUtil.*;
import static com.vasche.util.constants.ErrorCodes.*;

public class CreateUserValidator implements Validator<CreateUserDto> {
    private static final Logger LOGGER = Logger.getLogger(CreateUserValidator.class);
    private final UserRepository userRepository;

    public CreateUserValidator() {
        userRepository = new UserRepository();
    }

    @Override
    public ValidationResult isValid(CreateUserDto createUserDto) {
        try {
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
            if (userRepository.findByEmail(createUserDto.getEmail()).isPresent()) {
                validationResult.add(Error.of(NOT_UNIQUE_EMAIL, "Email already exists"));
            }
            if (!isCorrectPassword(createUserDto.getPassword())) {
                validationResult.add(Error.of(INVALID_PASSWORD, "Password is invalid"));
            }
            if (Role.find(createUserDto.getRole()).isEmpty()) {
                validationResult.add(Error.of(INVALID_ROLE, "Role is invalid"));
            }

            return validationResult;
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ValidatorException("Couldn't verify if createUserDto is valid", e);
        }
    }

}
