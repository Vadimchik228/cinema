package com.vasche.service;

import com.vasche.dto.user.CreateUserDto;
import com.vasche.dto.user.UserDto;
import com.vasche.entity.User;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ServiceException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.user.CreateUserMapper;
import com.vasche.mapper.user.UserMapper;
import com.vasche.repository.UserRepository;
import com.vasche.validator.CreateUserValidator;
import com.vasche.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends ServiceTestBase {

    @Mock
    private CreateUserValidator createUserValidator;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CreateUserMapper createUserMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateIfValidationPassed() throws RepositoryException, ServiceException {

        CreateUserDto createUserDto = getCreateUserDto();
        User user = getUser();
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        doReturn(validationResult).when(createUserValidator).isValid(createUserDto);

        when(validationResult.isValid())
                .thenReturn(true);
        doReturn(user).when(createUserMapper).mapFrom(createUserDto);

        doReturn(user).when(userRepository).save(user);

        Integer actualResult = userService.create(createUserDto);

        assertThat(actualResult)
                .isEqualTo(1);
    }

    @Test
    void testCreateIfValidationFailed() {
        CreateUserDto createUserDto = getCreateUserDto();
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        when(createUserValidator.isValid(createUserDto))
                .thenReturn(validationResult);
        when(validationResult.isValid())
                .thenReturn(false);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> userService.create(createUserDto));
    }

    @Test
    void testUpdateIfValidationFailed() {
        CreateUserDto createUserDto = getCreateUserDto();
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
        when(createUserValidator.isValid(createUserDto))
                .thenReturn(validationResult);
        when(validationResult.isValid())
                .thenReturn(false);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> userService.create(createUserDto));
    }

    @Test
    void testFindByIdIfUserExists() throws RepositoryException, ServiceException {
        User user = getUser();
        UserDto userDto = getUserDto();
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(userMapper.mapFrom(user))
                .thenReturn(userDto);

        final Optional<UserDto> actualResult = userService.findById(user.getId());

        assertThat(actualResult)
                .isPresent();
        assertThat(actualResult.get())
                .isEqualTo(userDto);
    }

    @Test
    void testFindByIdIfUserDoesNotExist() throws RepositoryException, ServiceException {
        when(userRepository.findById(1))
                .thenReturn(Optional.empty());

        final Optional<UserDto> actualResult = userService.findById(1);

        assertThat(actualResult)
                .isEmpty();
        verifyNoInteractions(userMapper);
    }

    @Test
    void testFindByReservationIdIfUserExists() throws RepositoryException, ServiceException {
        User user = getUser();
        UserDto userDto = getUserDto();
        when(userRepository.findByReservationId(1))
                .thenReturn(Optional.of(user));
        when(userMapper.mapFrom(user))
                .thenReturn(userDto);

        final Optional<UserDto> actualResult = userService.findByReservationId(1);

        assertThat(actualResult)
                .isPresent();
        assertThat(actualResult.get())
                .isEqualTo(userDto);
    }

    @Test
    void testFindByReservationIdIfUserDoesNotExist() throws RepositoryException, ServiceException {
        when(userRepository.findByReservationId(1))
                .thenReturn(Optional.empty());

        final Optional<UserDto> actualResult = userService.findByReservationId(1);

        assertThat(actualResult)
                .isEmpty();
        verifyNoInteractions(userMapper);
    }

    @Test
    void testFindAll() throws RepositoryException, ServiceException {

        User user = getUser();
        List<User> users = List.of(user);
        UserDto userDto = getUserDto();
        when(userRepository.findAll())
                .thenReturn(users);
        when(userMapper.mapFrom(user))
                .thenReturn(userDto);

        final List<UserDto> actualResult = userService.findAll();

        assertThat(actualResult)
                .hasSize(1);
        actualResult.stream().map(UserDto::getId)
                .forEach(id -> assertThat(id).isEqualTo(1));
    }
}
