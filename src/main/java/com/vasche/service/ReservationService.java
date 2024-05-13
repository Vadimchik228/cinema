package com.vasche.service;

import com.vasche.dto.reservation.CreateReservationDto;
import com.vasche.dto.reservation.ReservationAllDataDto;
import com.vasche.dto.reservation.ReservationDto;
import com.vasche.entity.*;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ReservationAllDataException;
import com.vasche.exception.ServiceException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.reservation.CreateReservationMapper;
import com.vasche.mapper.reservation.ReservationAllDataMapper;
import com.vasche.mapper.reservation.ReservationMapper;
import com.vasche.repository.*;
import com.vasche.validator.CreateReservationValidator;
import com.vasche.validator.ValidationResult;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


public class ReservationService {
    private static final Logger LOGGER = Logger.getLogger(ReservationService.class);
    private final ReservationRepository reservationRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final LineRepository lineRepository;
    private final HallRepository hallRepository;
    private final ReservationMapper reservationMapper;
    private final ReservationAllDataMapper reservationAllDataMapper;
    private final CreateReservationMapper createReservationMapper;
    private final CreateReservationValidator createReservationValidator;

    public ReservationService() {
        this(new ReservationRepository(),
                new ScreeningRepository(),
                new SeatRepository(),
                new UserRepository(),
                new MovieRepository(),
                new LineRepository(),
                new HallRepository(),
                new ReservationMapper(),
                new ReservationAllDataMapper(),
                new CreateReservationMapper(),
                new CreateReservationValidator());
    }

    public ReservationService(ReservationRepository reservationRepository,
                              ScreeningRepository screeningRepository,
                              SeatRepository seatRepository,
                              UserRepository userRepository,
                              MovieRepository movieRepository,
                              LineRepository lineRepository,
                              HallRepository hallRepository,
                              ReservationMapper reservationMapper,
                              ReservationAllDataMapper reservationAllDataMapper,
                              CreateReservationMapper createReservationMapper,
                              CreateReservationValidator createReservationValidator) {
        this.reservationRepository = reservationRepository;
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.lineRepository = lineRepository;
        this.hallRepository = hallRepository;
        this.reservationMapper = reservationMapper;
        this.reservationAllDataMapper = reservationAllDataMapper;
        this.createReservationMapper = createReservationMapper;
        this.createReservationValidator = createReservationValidator;
    }


    public Integer create(final CreateReservationDto createReservationDto) throws ServiceException {
        final ValidationResult validationResult = createReservationValidator.isValid(createReservationDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        Reservation reservation = createReservationMapper.mapFrom(createReservationDto);
        try {
            return reservationRepository.save(reservation).getId();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't save new reservation", e);
        }
    }

    public Optional<ReservationDto> findById(final Integer reservationId) throws ServiceException {
        try {
            return reservationRepository.findById(reservationId)
                    .map(reservationMapper::mapFrom);
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't find reservationDto by id", e);
        }
    }

    public Optional<ReservationAllDataDto> findWithALlDataById(final Integer reservationId) throws ServiceException {
        try {
            return reservationRepository.findById(reservationId)
                    .map(this::getReservationAllDataDto);
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't find reservationDto with all data by id", e);
        }
    }

    public List<ReservationAllDataDto> findAllWithAllDataByUserId(final Integer userId) throws ServiceException {
        try {
            return reservationRepository.findAllByUserId(userId).stream()
                    .map(this::getReservationAllDataDto)
                    .toList();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't find all reservationDtos with all data by userId", e);
        }
    }

    protected ReservationAllDataDto getReservationAllDataDto(Reservation reservation) {
        try {
            Optional<User> user = userRepository.findById(reservation.getUserId());
            Optional<Screening> screening = screeningRepository.findById(reservation.getScreeningId());
            Optional<Seat> seat = seatRepository.findById(reservation.getSeatId());
            if (user.isPresent() && screening.isPresent() && seat.isPresent()) {
                Optional<Movie> movie = movieRepository.findById(screening.get().getMovieId());
                Optional<Hall> hall = hallRepository.findById(screening.get().getHallId());
                Optional<Line> line = lineRepository.findById(seat.get().getLineId());
                if (movie.isPresent() && hall.isPresent() && line.isPresent()) {
                    return reservationAllDataMapper.mapFrom(reservation, user.get(),
                            screening.get(), movie.get(), hall.get(), line.get(), seat.get());
                }
            }
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ReservationAllDataException("Couldn't create reservationDto with all data by id", e);
        }
        return null;
    }

    public boolean delete(final Integer reservationId) throws ServiceException {
        try {
            return reservationRepository.delete(reservationId);
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't delete reservation by id", e);
        }
    }

    public BigDecimal countTotalIncome() throws ServiceException {
        try {
            BigDecimal income = BigDecimal.valueOf(0);
            List<Reservation> reservations = reservationRepository.findAll();
            for (Reservation reservation : reservations) {
                Optional<Screening> screening = screeningRepository.findById(reservation.getScreeningId());
                if (screening.isPresent()) {
                    income = income.add(screening.get().getPrice());
                }
            }
            return income;
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't count total income", e);
        }
    }

    public BigDecimal countIncomeByScreeningId(final Integer screeningId) throws ServiceException {
        try {
            BigDecimal income = BigDecimal.valueOf(0);
            List<Reservation> reservations = reservationRepository.findAll().stream()
                    .filter(reservation -> reservation.getScreeningId() == screeningId)
                    .toList();
            for (Reservation reservation : reservations) {
                Optional<Screening> screening = screeningRepository.findById(reservation.getScreeningId());
                if (screening.isPresent()) {
                    income = income.add(screening.get().getPrice());
                }
            }
            return income;
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't count income by screeningId", e);
        }
    }

}
