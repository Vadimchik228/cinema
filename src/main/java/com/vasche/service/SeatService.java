package com.vasche.service;

import com.vasche.repository.SeatRepository;
import com.vasche.dto.seat.CreateSeatDto;
import com.vasche.dto.seat.SeatDto;
import com.vasche.entity.Seat;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.seat.CreateSeatMapper;
import com.vasche.mapper.seat.SeatMapper;
import com.vasche.validator.CreateSeatValidator;
import com.vasche.validator.ValidationResult;

import java.util.List;
import java.util.Optional;

public class SeatService {

    private final CreateSeatValidator createSeatValidator;

    private final SeatRepository seatDao;

    private final CreateSeatMapper createSeatMapper;

    private final SeatMapper seatMapper;

    public SeatService() {
        this(new CreateSeatValidator(),
                new SeatRepository(),
                new CreateSeatMapper(),
                new SeatMapper());
    }

    public SeatService(CreateSeatValidator createSeatValidator,
                        SeatRepository seatDao,
                        CreateSeatMapper createSeatMapper,
                        SeatMapper seatMapper) {
        this.createSeatValidator = createSeatValidator;
        this.seatDao = seatDao;
        this.createSeatMapper = createSeatMapper;
        this.seatMapper = seatMapper;
    }


    public Integer create(final CreateSeatDto createSeatDto) throws RepositoryException {
        final ValidationResult validationResult = createSeatValidator.isValid(createSeatDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        Seat seat = createSeatMapper.mapFrom(createSeatDto);
        return seatDao.save(seat).getId();
    }

    public void update(final CreateSeatDto createSeatDto) throws RepositoryException {
        final ValidationResult validationResult = createSeatValidator.isValid(createSeatDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        Seat seat = createSeatMapper.mapFrom(createSeatDto);
        seatDao.update(seat);
    }

    public boolean delete(final Integer seatId) throws RepositoryException {
        return seatDao.delete(seatId);
    }

    public Optional<SeatDto> findById(final Integer seatId) throws RepositoryException {
        return seatDao.findById(seatId)
                .map(seatMapper::mapFrom);
    }

    public Optional<SeatDto> findByReservationId(final Integer reservationId) throws RepositoryException {
        return seatDao.findByReservationId(reservationId)
                .map(seatMapper::mapFrom);
    }


    public List<SeatDto> findAll() throws RepositoryException {
        return seatDao.findAll().stream()
                .map(seatMapper::mapFrom)
                .toList();
    }

    public List<SeatDto> findAllByLineId(final Integer lineId) throws RepositoryException {
        return seatDao.findAllByLineId(lineId).stream()
                .map(seatMapper::mapFrom)
                .toList();
    }

    public List<SeatDto> findAllByHallId(final Integer hallId) throws RepositoryException {
        return seatDao.findAllByHallId(hallId).stream()
                .map(seatMapper::mapFrom)
                .toList();
    }

    public List<SeatDto> findAllAvailable(final Integer screeningId, final Integer hallId) throws RepositoryException {
        return seatDao.findAllAvailable(screeningId, hallId).stream()
                .map(seatMapper::mapFrom)
                .toList();
    }
}
