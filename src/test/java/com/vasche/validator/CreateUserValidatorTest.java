package com.vasche.validator;

import com.vasche.dto.user.CreateUserDto;
import com.vasche.entity.Role;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.vasche.util.constants.ErrorCodes.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CreateUserValidatorTest {

    private final CreateUserValidator validator = new CreateUserValidator();

    @Test
    void shouldPassValidation() {

        CreateUserDto dto = CreateUserDto.builder()
                .role(Role.CLIENT.name())
                .password("Vadim123!@")
                .firstName("Vadim")
                .lastName("Schebetovskiy")
                .email("schebetovskiy@gmail.com")
                .build();

        ValidationResult actualResult = validator.isValid(dto);

        assertTrue(actualResult.getErrors().isEmpty());
    }

    @Test
    void invalidFirstname() {

        CreateUserDto dto = CreateUserDto.builder()
                .role(Role.CLIENT.name())
                .password("Vadim123!@")
                .lastName("Schebetovskiy")
                .email("schebetovskiy@gmail.com")
                .build();


        ValidationResult actualResult = validator.isValid(dto);

        AssertionsForInterfaceTypes.assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_FIRST_NAME);
    }

    @Test
    void invalidLastName() {

        CreateUserDto dto = CreateUserDto.builder()
                .role(Role.CLIENT.name())
                .password("Vadim123!@")
                .firstName("Vadim")
                .email("schebetovskiy@gmail.com")
                .build();


        ValidationResult actualResult = validator.isValid(dto);

        AssertionsForInterfaceTypes.assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_LAST_NAME);
    }

    @Test
    void invalidEmail() {

        CreateUserDto dto = CreateUserDto.builder()
                .role(Role.CLIENT.name())
                .password("Vadim123!@")
                .firstName("Vadim")
                .lastName("Schebetovskiy")
                .email("schebetovskiy@gmail")
                .build();


        ValidationResult actualResult = validator.isValid(dto);

        AssertionsForInterfaceTypes.assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_EMAIL);
    }

    @Test
    void invalidPassword() {

        CreateUserDto dto = CreateUserDto.builder()
                .role(Role.CLIENT.name())
                .password("Vadim")
                .firstName("Vadim")
                .lastName("Schebetovskiy")
                .email("schebetovskiy@gmail.com")
                .build();


        ValidationResult actualResult = validator.isValid(dto);

        AssertionsForInterfaceTypes.assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_PASSWORD);
    }
    

    @Test
    void invalidRole() {

        CreateUserDto dto = CreateUserDto.builder()
                .password("Vadim123!@")
                .firstName("Vadim")
                .lastName("Schebetovskiy")
                .email("schebetovskiy@gmail.com")
                .build();

        ValidationResult actualResult = validator.isValid(dto);

        AssertionsForInterfaceTypes.assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_ROLE);
    }


    @Test
    void invalidFirstNameLastNameEmailPasswordRole() {
        CreateUserDto dto = CreateUserDto.builder()
                .build();

        ValidationResult actualResult = validator.isValid(dto);

        Assertions.assertThat(actualResult.getErrors()).hasSize(5);
        List<String> errorCodes = actualResult.getErrors().stream()
                .map(Error::getCode)
                .toList();
        Assertions.assertThat(errorCodes).contains(INVALID_FIRST_NAME, INVALID_LAST_NAME,
                INVALID_EMAIL, INVALID_PASSWORD, INVALID_ROLE);
    }


}
