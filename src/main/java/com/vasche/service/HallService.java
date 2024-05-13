package com.vasche.service;

import com.vasche.dto.hall.HallAllDataDto;
import com.vasche.dto.hall.HallDto;
import com.vasche.exception.LineServiceException;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ServiceException;
import com.vasche.mapper.hall.HallAllDataMapper;
import com.vasche.mapper.hall.HallMapper;
import com.vasche.repository.HallRepository;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class HallService {
    private static final Logger LOGGER = Logger.getLogger(HallService.class);
    private final HallRepository hallRepository;
    private final HallMapper hallMapper;
    private final HallAllDataMapper hallAllDataMapper;
    private final LineService lineService;

    public HallService() {
        this(new HallRepository(),
                new HallMapper(),
                new HallAllDataMapper(),
                new LineService());
    }

    public HallService(HallRepository hallRepository,
                       HallMapper hallMapper,
                       HallAllDataMapper hallAllDataMapper,
                       LineService lineService) {
        this.hallRepository = hallRepository;
        this.hallMapper = hallMapper;
        this.hallAllDataMapper = hallAllDataMapper;
        this.lineService = lineService;
    }

    public Optional<HallDto> findById(final Integer hallId) throws ServiceException {
        try {
            return hallRepository.findById(hallId)
                    .map(hallMapper::mapFrom);
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get hallDto by id", e);
        }
    }

    public List<HallDto> findAll() throws ServiceException {
        try {
            return hallRepository.findAll().stream()
                    .map(hallMapper::mapFrom)
                    .toList();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get all hallDtos", e);
        }
    }

    public List<HallAllDataDto> findAllWithAllData(final Integer screeningId) throws ServiceException {
        try {
            return hallRepository.findAll().stream()
                    .map(hall -> {
                        try {
                            return hallAllDataMapper.mapFrom(hall,
                                    lineService.findAllWithAllDataByHallId(hall.getId(), screeningId));
                        } catch (ServiceException e) {
                            LOGGER.error(e.getMessage());
                            throw new LineServiceException("Couldn't get all lineDtos with all data by hallId", e);
                        }
                    })
                    .toList();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get all hallDtos with all data", e);
        }
    }

    public Optional<HallAllDataDto> findWithAllDataById(final Integer hallId, final Integer screeningId) throws ServiceException {
        try {
            return hallRepository.findById(hallId)
                    .map(hall -> {
                        try {
                            return hallAllDataMapper.mapFrom(hall,
                                    lineService.findAllWithAllDataByHallId(hall.getId(), screeningId));
                        } catch (ServiceException e) {
                            LOGGER.error(e.getMessage());
                            throw new LineServiceException("Couldn't get lineDto with all data by hallId", e);
                        }
                    });
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get hallDto with all data by id", e);
        }
    }

    public int countNumberOfHalls() throws ServiceException {
        try {
            return hallRepository.findAll().size();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get number of halls", e);
        }
    }
}
