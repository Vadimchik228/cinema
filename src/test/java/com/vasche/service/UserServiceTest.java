package com.vasche.service;

import com.vasche.dao.UserDao;
import com.vasche.dto.user.CreateUserDto;
import com.vasche.entity.Role;
import com.vasche.entity.User;
import com.vasche.exception.DaoException;
import com.vasche.mapper.user.CreateUserMapper;
import com.vasche.mapper.user.UserMapper;
import com.vasche.validator.CreateUserValidator;
import com.vasche.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class UserServiceTest {

    @Mock
    private CreateUserValidator createUserValidator;

    @Mock
    private UserDao userDao;

    @Mock
    private CreateUserMapper createUserMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateIfValidationPassed() throws DaoException {

        CreateUserDto createUserDto = getCreateUserDto();
        User user = getUser();
        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
//        when(createUserValidator.isValid(createUserDto))
//                .thenReturn(validationResult);
        doReturn(validationResult).when(createUserValidator).isValid(createUserDto);

        when(validationResult.isValid())
                .thenReturn(true);
//        when(createUserMapper.mapFrom(createUserDto))
//                .thenReturn(user);
        doReturn(user).when(createUserMapper).mapFrom(createUserDto);
//        when(userDao.save(user))
//                .thenReturn(user);
        doReturn(user).when(userDao).save(user);

        Integer actualResult = userService.create(createUserDto);

        assertThat(actualResult)
                .isEqualTo(1);
    }

//    @Test
//    void testCreateIfValidationFailed() {
//        CreateUserDto createUserDto = getCreateUserDto();
//        UserEntity userEntity = getUserEntity();
//        ValidationResult validationResult = Mockito.mock(ValidationResult.class);
//        when(createUserValidator.isValid(createUserDto))
//                .thenReturn(validationResult);
//        when(validationResult.isValid())
//                .thenReturn(false);
//
//        assertThatExceptionOfType(ValidationException.class)
//                .isThrownBy(() -> userService.create(createUserDto));
//    }
//
//
//    @Test
//    void testLoginSuccess() {
//        UserEntity userEntity = getUserEntity();
//        UserDto userDto = getUserDto();
//
//        when(userDao.findByEmailAndPassword(userEntity.getEmail(), userEntity.getPassword()))
//                .thenReturn(Optional.of(userEntity));
//        when(userMapper.map(userEntity))
//                .thenReturn(userDto);
//
//        final Optional<UserDto> actualResult = userService.login(userEntity.getEmail(), userEntity.getPassword());
//
//        assertThat(actualResult).isPresent();
//        assertThat(actualResult.get()).isEqualTo(userDto);
//    }
//
//    @Test
//    void testLoginFailed() {
//        UserEntity userEntity = getUserEntity();
//
//        when(userDao.findByEmailAndPassword(userEntity.getEmail(), userEntity.getPassword()))
//                .thenReturn(Optional.empty());
//
//        final Optional<UserDto> actualResult = userService.login(userEntity.getEmail(), userEntity.getPassword());
//
//        assertThat(actualResult).isEmpty();
//        verifyNoInteractions(userMapper);
//    }
//
//    @Test
//    void testFindIdByNameIfUserExists() {
//        UserEntity userEntity = Mockito.mock(UserEntity.class);
//
//        when(userDao.findByUserName(userEntity.getName()))
//                .thenReturn(Optional.of(userEntity));
//        when(userEntity.getId())
//                .thenReturn(1);
//
//        final Optional<Integer> actualResult = userService.findIdByName(userEntity.getName());
//
//        assertThat(actualResult)
//                .isPresent();
//        assertThat(actualResult.get())
//                .isEqualTo(1);
//    }
//
//    @Test
//    void testFindByIdIfUserExists() {
//        UserEntity userEntity = getUserEntity();
//        UserDto userDto = getUserDto();
//        when(userDao.findById(1))
//                .thenReturn(Optional.of(userEntity));
//        when(userMapper.map(userEntity))
//                .thenReturn(userDto);
//
//        final Optional<UserDto> actualResult = userService.findById(1);
//
//        assertThat(actualResult)
//                .isPresent();
//        assertThat(actualResult.get())
//                .isEqualTo(userDto);
//    }
//
//    @Test
//    void testFindByIdIfUserNotExists() {
//        when(userDao.findById(1))
//                .thenReturn(Optional.empty());
//
//        final Optional<UserDto> actualResult = userService.findById(1);
//
//        assertThat(actualResult)
//                .isEmpty();
//        verifyNoInteractions(userMapper);
//    }
//
//    @Test
//    void testFindAll() {
//
//        UserEntity userEntity = getUserEntity();
//        List<UserEntity> users = List.of(userEntity);
//        UserDto userDto = getUserDto();
//        when(userDao.findAll())
//                .thenReturn(users);
//        when(userMapper.map(userEntity))
//                .thenReturn(userDto);
//
//        final List<UserDto> actualResult = userService.findAll();
//
//        assertThat(actualResult)
//                .hasSize(1);
//        actualResult.stream().map(UserDto::getId)
//                .forEach(id -> assertThat(id).isEqualTo(1));
//    }
//
//
//    private static UserDto getUserDto() {
//        return UserDto.builder()
//                .id(1)
//                .name("test")
//                .email("test.email@gmail.com")
//                .role(UserRole.USER)
//                .birthday(LocalDate.of(2020, 10, 10))
//                .build();
//    }


    private static User getUser() {
        return User.builder()
                .id(1)
                .password("vadim123@!")
                .email("schebetovskiy@gmail.com")
                .role(Role.CLIENT)
                .lastName("Schebetovskiy")
                .firstName("Vadim")
                .build();
    }

    private static CreateUserDto getCreateUserDto() {
        return CreateUserDto.builder()
                .password("vadim123@!")
                .email("schebetovskiy@gmail.com")
                .role(Role.CLIENT.name())
                .lastName("Schebetovskiy")
                .firstName("Vadim")
                .build();
    }
}
