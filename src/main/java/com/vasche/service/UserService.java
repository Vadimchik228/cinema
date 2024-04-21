package com.vasche.service;

import com.vasche.repository.UserRepository;
import com.vasche.dto.user.CreateUserDto;
import com.vasche.dto.user.UserDto;
import com.vasche.entity.User;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.user.CreateUserMapper;
import com.vasche.mapper.user.UserMapper;
import com.vasche.validator.CreateUserValidator;
import com.vasche.validator.ValidationResult;

import java.util.List;
import java.util.Optional;

public class UserService {

    private CreateUserValidator createUserValidator;

    private UserRepository userDao;

    private CreateUserMapper createUserMapper;

    private UserMapper userMapper;

    private UserService() {
        this(new CreateUserValidator(),
                new UserRepository(),
                new CreateUserMapper(),
                new UserMapper());
    }

    private UserService(CreateUserValidator createUserValidator,
                        UserRepository userDao,
                        CreateUserMapper createUserMapper,
                        UserMapper userMapper) {
        this.createUserValidator = createUserValidator;
        this.userDao = userDao;
        this.createUserMapper = createUserMapper;
        this.userMapper = userMapper;
    }


    public Integer create(final CreateUserDto createUserDto) throws RepositoryException {
        final ValidationResult validationResult = createUserValidator.isValid(createUserDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        User user = createUserMapper.mapFrom(createUserDto);
        return userDao.save(user).getId();
    }

    public void update(final CreateUserDto createUserDto) throws RepositoryException {
        final ValidationResult validationResult = createUserValidator.isValid(createUserDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        User user = createUserMapper.mapFrom(createUserDto);
        userDao.update(user);
    }


    public Optional<UserDto> login(final String email, final String password) throws RepositoryException {
        return userDao.findByEmailAndPassword(email, password)
                .map(userMapper::mapFrom);
    }

    public Optional<UserDto> findIdByEmail(final String email) throws RepositoryException {
        return userDao.findByEmail(email)
                .map(userMapper::mapFrom);
    }

    public Optional<UserDto> findById(final Integer userId) throws RepositoryException {
        return userDao.findById(userId)
                .map(userMapper::mapFrom);
    }

    public Optional<UserDto> findByReservationId(final Integer reservationId) throws RepositoryException {
        return userDao.findByReservationId(reservationId)
                .map(userMapper::mapFrom);
    }

    public List<UserDto> findAll() throws RepositoryException {
        return userDao.findAll().stream()
                .map(userMapper::mapFrom)
                .toList();
    }

    public List<UserDto> findAllByScreeningId(final Integer screeningId) throws RepositoryException {
        return userDao.findAllByScreeningId(screeningId).stream()
                .map(userMapper::mapFrom)
                .toList();
    }
}
