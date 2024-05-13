package com.vasche.service;

import com.vasche.dto.line.LineAllDataDto;
import com.vasche.dto.line.LineDto;
import com.vasche.dto.seat.SeatAllDataDto;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.SeatServiceException;
import com.vasche.exception.ServiceException;
import com.vasche.mapper.line.LineAllDataMapper;
import com.vasche.mapper.line.LineMapper;
import com.vasche.repository.LineRepository;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class LineService {
    private static final Logger LOGGER = Logger.getLogger(LineService.class);
    private final LineRepository lineRepository;
    private final LineMapper lineMapper;
    private final LineAllDataMapper lineAllDataMapper;
    private final SeatService seatService;

    public LineService() {
        this(new LineRepository(),
                new LineMapper(),
                new LineAllDataMapper(),
                new SeatService());
    }

    public LineService(LineRepository lineRepository,
                       LineMapper lineMapper,
                       LineAllDataMapper lineAllDataMapper,
                       SeatService seatService) {
        this.lineRepository = lineRepository;
        this.lineMapper = lineMapper;
        this.lineAllDataMapper = lineAllDataMapper;
        this.seatService = seatService;
    }

    public Optional<LineDto> findById(final Integer lineId) throws ServiceException {
        try {
            return lineRepository.findById(lineId)
                    .map(lineMapper::mapFrom);
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get lineDto by id", e);
        }
    }

    public List<LineDto> findAll() throws ServiceException {
        try {
            return lineRepository.findAll().stream()
                    .map(lineMapper::mapFrom)
                    .toList();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get lineDto by seatId", e);
        }
    }

    public List<LineAllDataDto> findAllWithAllDataByHallId(final Integer hallId, final Integer screeningId) throws ServiceException {
        try {
            return lineRepository.findAllByHallId(hallId).stream()
                    .map(line -> {
                        List<SeatAllDataDto> seats;
                        try {
                            seats = seatService.findAllWithAllDataByLineId(line.getId(), screeningId);
                        } catch (ServiceException e) {
                            LOGGER.error(e.getMessage());
                            throw new SeatServiceException("Couldn't get seatDtos with all data by lineId", e);
                        }
                        return lineAllDataMapper.mapFrom(line, seats);
                    })
                    .toList();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get lineDtos with all data by hallId", e);
        }
    }
}
