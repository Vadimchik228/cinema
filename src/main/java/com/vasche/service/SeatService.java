package com.vasche.service;

import com.vasche.dto.seat.SeatAllDataDto;
import com.vasche.dto.seat.SeatDto;
import com.vasche.entity.Seat;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ServiceException;
import com.vasche.mapper.seat.SeatAllDataMapper;
import com.vasche.mapper.seat.SeatMapper;
import com.vasche.repository.SeatRepository;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SeatService {
    private static final Logger LOGGER = Logger.getLogger(SeatService.class);
    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;
    private final SeatAllDataMapper seatAllDataMapper;

    public SeatService() {
        this(new SeatRepository(),
                new SeatMapper(),
                new SeatAllDataMapper());
    }

    public SeatService(
            SeatRepository seatRepository,
            SeatMapper seatMapper,
            SeatAllDataMapper seatAllDataMapper) {
        this.seatRepository = seatRepository;
        this.seatMapper = seatMapper;
        this.seatAllDataMapper = seatAllDataMapper;
    }

    public Optional<SeatDto> findById(final Integer seatId) throws ServiceException {
        try {
            return seatRepository.findById(seatId)
                    .map(seatMapper::mapFrom);
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get seatDto by id", e);
        }
    }

    public Optional<SeatDto> findByReservationId(final Integer reservationId) throws ServiceException {
        try {
            return seatRepository.findByReservationId(reservationId)
                    .map(seatMapper::mapFrom);
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get seatDto by reservationId", e);
        }
    }

    public List<SeatDto> findAll() throws ServiceException {
        try {
            return seatRepository.findAll().stream()
                    .map(seatMapper::mapFrom)
                    .toList();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get all seatDtos", e);
        }
    }

    public List<SeatAllDataDto> findAllWithAllDataByLineId(final Integer lineId, final Integer screeningId) throws ServiceException {
        try {
            List<Seat> availableSeats;
            if (screeningId != null) {
                availableSeats = seatRepository.findAllAvailableByLineId(lineId, screeningId);
            } else {
                availableSeats = Collections.emptyList();
            }

            return seatRepository.findAllByLineId(lineId).stream()
                    .map(seat -> {
                        boolean isReserved = !availableSeats.contains(seat);
                        return seatAllDataMapper.mapFrom(seat, isReserved);
                    })
                    .toList();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get all seatDtos with all data by lineId", e);
        }
    }
}
