package com.vasche.service;

import com.vasche.dto.user.CreateUserDto;
import com.vasche.dto.user.UserDto;
import com.vasche.dto.user.UserWithSeatDto;
import com.vasche.entity.Reservation;
import com.vasche.entity.Seat;
import com.vasche.entity.User;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ServiceException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.user.CreateUserMapper;
import com.vasche.mapper.user.UserMapper;
import com.vasche.mapper.user.UserWithSeatMapper;
import com.vasche.repository.ReservationRepository;
import com.vasche.repository.SeatRepository;
import com.vasche.repository.UserRepository;
import com.vasche.util.PasswordHasher;
import com.vasche.validator.CreateUserValidator;
import com.vasche.validator.Error;
import com.vasche.validator.ValidationResult;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.vasche.util.constants.ErrorCodes.INVALID_EMAIL;
import static com.vasche.util.constants.ErrorCodes.INVALID_PASSWORD;

public class UserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class);
    private static final String SALT = "salt";
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;
    private final UserMapper userMapper;
    private final UserWithSeatMapper userWithSeatMapper;
    private final CreateUserMapper createUserMapper;
    private final CreateUserValidator createUserValidator;

    public UserService() {
        this(new UserRepository(),
                new ReservationRepository(),
                new SeatRepository(),
                new UserMapper(),
                new UserWithSeatMapper(),
                new CreateUserMapper(),
                new CreateUserValidator());
    }

    public UserService(UserRepository userDao,
                       ReservationRepository reservationRepository,
                       SeatRepository seatRepository,
                       UserMapper userMapper,
                       UserWithSeatMapper userWithSeatMapper,
                       CreateUserMapper createUserMapper,
                       CreateUserValidator createUserValidator) {
        this.createUserValidator = createUserValidator;
        this.userRepository = userDao;
        this.createUserMapper = createUserMapper;
        this.userMapper = userMapper;
        this.userWithSeatMapper = userWithSeatMapper;
        this.reservationRepository = reservationRepository;
        this.seatRepository = seatRepository;
    }

    public Integer create(final CreateUserDto createUserDto) throws ServiceException {
        final ValidationResult validationResult = createUserValidator.isValid(createUserDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        User user = createUserMapper.mapFrom(createUserDto);
        user.setPassword(getPasswordHash(user.getPassword()));
        try {
            return userRepository.save(user).getId();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't save user", e);
        }
    }

    public Optional<UserDto> findById(final Integer userId) throws ServiceException {
        try {
            return userRepository.findById(userId)
                    .map(userMapper::mapFrom);
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get userDto by id", e);
        }
    }

    public Optional<UserDto> findByReservationId(final Integer reservationId) throws ServiceException {
        try {
            return userRepository.findByReservationId(reservationId)
                    .map(userMapper::mapFrom);
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get userDto by reservationId", e);
        }
    }

    public List<UserDto> findAll() throws ServiceException {
        try {
            return userRepository.findAll().stream()
                    .map(userMapper::mapFrom)
                    .toList();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get all userDtos", e);
        }
    }

    public List<UserWithSeatDto> findAllBookmakersByScreeningId(final Integer screeningId) throws ServiceException {
        try {
            List<UserWithSeatDto> userWithSeatDtos = new ArrayList<>(List.of());
            List<Reservation> reservations = reservationRepository.findAllByScreeningId(screeningId);
            for (Reservation reservation : reservations) {
                Optional<User> user = userRepository.findById(reservation.getUserId());
                Optional<Seat> seat = seatRepository.findById(reservation.getSeatId());
                if (user.isPresent() && seat.isPresent()) {
                    userWithSeatDtos.add(userWithSeatMapper.mapFrom(user.get(), seat.get().getNumber(), reservation.getId()));
                }
            }
            userWithSeatDtos.sort(Comparator.comparingInt(UserWithSeatDto::getSeatNumber));
            return userWithSeatDtos;
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get all bookmakers by screeningId", e);
        }
    }

    public Optional<UserDto> login(final String email, final String password) throws ServiceException {
        try {
            Optional<User> user = userRepository.findByEmail(email);

            if (user.isEmpty()) {
                throw new ValidationException(List.of(Error.of(INVALID_EMAIL, "There is no such email in database")));
            } else if (!checkPassword(password, user.get().getPassword())) {
                throw new ValidationException(List.of(Error.of(INVALID_PASSWORD, "Password is incorrect")));
            }

            return user.map(userMapper::mapFrom);
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get userDto by email", e);
        }
    }

    private String getPasswordHash(String password) {
        return PasswordHasher.hash(password, SALT);
    }

    private Boolean checkPassword(String candidate, String hashed) {
        return PasswordHasher.check(candidate, SALT, hashed);
    }
}
