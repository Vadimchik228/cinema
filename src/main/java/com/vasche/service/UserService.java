package com.vasche.service;

import com.vasche.dao.UserDao;
import com.vasche.dto.user.CreateUserDto;
import com.vasche.dto.user.UserDto;
import com.vasche.exception.DaoException;
import com.vasche.exception.ServiceException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.user.CreateUserMapper;
import com.vasche.mapper.user.UserMapper;
import com.vasche.validator.CreateUserValidator;
import lombok.NoArgsConstructor;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UserService {

    private static final UserService INSTANCE = new UserService();

    private CreateUserValidator createUserValidator = CreateUserValidator.getInstance();
    private UserDao usersDao = UserDao.getInstance();
    private CreateUserMapper createUserMapper = CreateUserMapper.getInstance();
    private UserMapper userMapper = UserMapper.getInstance();

    public Optional<UserDto> login(String email, String password) {
        try {
            return usersDao.findByEmailAndPassword(email, password)
                    .map(userMapper::mapFrom);
        } catch (DaoException e) {
            throw new ServiceException("Couldn't login user");
        }
    }

    public Integer create(CreateUserDto userDto) {
        var validationResult = createUserValidator.isValid(userDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        var usersEntity = createUserMapper.mapFrom(userDto);
        try {
            usersDao.save(usersEntity);
        } catch (DaoException e) {
            throw new ServiceException("Couldn't create user");
        }
        return usersEntity.getId();
    }

    public static UserService getInstance() {
        return INSTANCE;
    }
}
