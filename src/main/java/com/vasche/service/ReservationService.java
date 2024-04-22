package com.vasche.service;

import com.vasche.repository.ReservationRepository;
import com.vasche.dto.reservation.CreateReservationDto;
import com.vasche.dto.reservation.ReservationDto;
import com.vasche.entity.Reservation;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.reservation.CreateReservationMapper;
import com.vasche.mapper.reservation.ReservationMapper;
import com.vasche.validator.CreateReservationValidator;
import com.vasche.validator.ValidationResult;

import java.util.List;
import java.util.Optional;


public class ReservationService {
    private final CreateReservationValidator createReservationValidator;

    private final ReservationRepository reservationDao;

    private final CreateReservationMapper createReservationMapper;

    private final ReservationMapper reservationMapper;

    public ReservationService() {
        this(new CreateReservationValidator(),
                new ReservationRepository(),
                new CreateReservationMapper(),
                new ReservationMapper());
    }

    public ReservationService(CreateReservationValidator createReservationValidator,
                               ReservationRepository reservationDao,
                               CreateReservationMapper createReservationMapper,
                               ReservationMapper reservationMapper) {
        this.createReservationValidator = createReservationValidator;
        this.reservationDao = reservationDao;
        this.createReservationMapper = createReservationMapper;
        this.reservationMapper = reservationMapper;
    }


    public Integer create(final CreateReservationDto createReservationDto) throws RepositoryException {
        final ValidationResult validationResult = createReservationValidator.isValid(createReservationDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        Reservation reservation = createReservationMapper.mapFrom(createReservationDto);
        return reservationDao.save(reservation).getId();
    }

    public boolean delete(final Integer reservationId) throws RepositoryException {
        return reservationDao.delete(reservationId);
    }

    public Optional<ReservationDto> findById(final Integer reservationId) throws RepositoryException {
        return reservationDao.findById(reservationId)
                .map(reservationMapper::mapFrom);
    }

    public List<ReservationDto> findAll() throws RepositoryException {
        return reservationDao.findAll().stream()
                .map(reservationMapper::mapFrom)
                .toList();
    }

    public List<ReservationDto> findAllByScreeningId(final Integer screeningId) throws RepositoryException {
        return reservationDao.findAllByScreeningId(screeningId).stream()
                .map(reservationMapper::mapFrom)
                .toList();
    }

    public List<ReservationDto> findAllByUserId(final Integer userId) throws RepositoryException {
        return reservationDao.findAllByUserId(userId).stream()
                .map(reservationMapper::mapFrom)
                .toList();
    }

}
