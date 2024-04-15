package com.vasche.validator;

import com.vasche.dao.UserDao;
import com.vasche.dto.user.CreateUserDto;
import com.vasche.entity.Role;
import com.vasche.entity.User;
import com.vasche.exception.DaoException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.vasche.util.constants.ErrorCodes.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class CreateUserValidatorTest {
    @InjectMocks
    private CreateUserValidator validator = CreateUserValidator.getInstance();
    @Mock
    private UserDao userDao = UserDao.getInstance();

    @Test
    void shouldPassValidation() throws DaoException {

        CreateUserDto dto = CreateUserDto.builder()
                .role(Role.CLIENT.name())
                .password("Vadim123!@")
                .firstName("Vadim")
                .lastName("Schebetovskiy")
                .email("schebetovskiy@gmail.com")
                .build();

        doReturn(Optional.empty()).when(userDao).findByEmail(dto.getEmail());

        ValidationResult actualResult = validator.isValid(dto);

        assertTrue(actualResult.getErrors().isEmpty());
    }

    @Test
    void invalidFirstname() throws DaoException {

        CreateUserDto dto = CreateUserDto.builder()
                .role(Role.CLIENT.name())
                .password("Vadim123!@")
                .lastName("Schebetovskiy")
                .email("schebetovskiy@gmail.com")
                .build();

        doReturn(Optional.empty()).when(userDao).findByEmail(dto.getEmail());

        ValidationResult actualResult = validator.isValid(dto);

        AssertionsForInterfaceTypes.assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_FIRST_NAME);
    }

    @Test
    void invalidLastName() throws DaoException {

        CreateUserDto dto = CreateUserDto.builder()
                .role(Role.CLIENT.name())
                .password("Vadim123!@")
                .firstName("Vadim")
                .email("schebetovskiy@gmail.com")
                .build();

        doReturn(Optional.empty()).when(userDao).findByEmail(dto.getEmail());

        ValidationResult actualResult = validator.isValid(dto);

        AssertionsForInterfaceTypes.assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_LAST_NAME);
    }

    @Test
    void invalidEmail() throws DaoException {

        CreateUserDto dto = CreateUserDto.builder()
                .role(Role.CLIENT.name())
                .password("Vadim123!@")
                .firstName("Vadim")
                .lastName("Schebetovskiy")
                .email("schebetovskiy@gmail")
                .build();

        doReturn(Optional.empty()).when(userDao).findByEmail(dto.getEmail());

        ValidationResult actualResult = validator.isValid(dto);

        AssertionsForInterfaceTypes.assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_EMAIL);
    }

    @Test
    void invalidPassword() throws DaoException {

        CreateUserDto dto = CreateUserDto.builder()
                .role(Role.CLIENT.name())
                .password("Vadim")
                .firstName("Vadim")
                .lastName("Schebetovskiy")
                .email("schebetovskiy@gmail.com")
                .build();

        doReturn(Optional.empty()).when(userDao).findByEmail(dto.getEmail());

        ValidationResult actualResult = validator.isValid(dto);

        AssertionsForInterfaceTypes.assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_PASSWORD);
    }

    @Test
    void notUniqueEmail() throws DaoException {

        CreateUserDto dto = CreateUserDto.builder()
                .role(Role.CLIENT.name())
                .password("Vadim123!@")
                .firstName("Vadim")
                .lastName("Schebetovskiy")
                .email("schebetovskiy@gmail.com")
                .build();

        var user = getUser();

        doReturn(Optional.of(user)).when(userDao).findByEmail(dto.getEmail());

        ValidationResult actualResult = validator.isValid(dto);

        AssertionsForInterfaceTypes.assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(NOT_UNIQUE_EMAIL);
    }

    @Test
    void invalidRole() throws DaoException {

        CreateUserDto dto = CreateUserDto.builder()
                .password("Vadim123!@")
                .firstName("Vadim")
                .lastName("Schebetovskiy")
                .email("schebetovskiy@gmail.com")
                .build();

        doReturn(Optional.empty()).when(userDao).findByEmail(dto.getEmail());

        ValidationResult actualResult = validator.isValid(dto);

        AssertionsForInterfaceTypes.assertThat(actualResult.getErrors()).hasSize(1);
        AssertionsForClassTypes.assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(INVALID_ROLE);
    }


    @Test
    void invalidFirstNameLastNameEmailPasswordRole() throws DaoException {
        CreateUserDto dto = CreateUserDto.builder()
                .build();

        doReturn(Optional.empty()).when(userDao).findByEmail(dto.getEmail());

        ValidationResult actualResult = validator.isValid(dto);

        Assertions.assertThat(actualResult.getErrors()).hasSize(5);
        List<String> errorCodes = actualResult.getErrors().stream()
                .map(Error::getCode)
                .toList();
        Assertions.assertThat(errorCodes).contains(INVALID_FIRST_NAME, INVALID_LAST_NAME,
                INVALID_EMAIL, INVALID_PASSWORD, INVALID_ROLE);
    }

    private static User getUser() {
        return User.builder()
                .id(1)
                .firstName("Vadim")
                .lastName("Scheb")
                .role(Role.CLIENT)
                .email("schebetovskiy@gmail.com")
                .password("Vadim123!@#")
                .build();
    }
}
