package com.vasche.service;

import com.vasche.repository.HallRepository;
import com.vasche.dto.hall.CreateHallDto;
import com.vasche.dto.hall.HallDto;
import com.vasche.entity.Hall;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.hall.CreateHallMapper;
import com.vasche.mapper.hall.HallMapper;
import com.vasche.validator.CreateHallValidator;
import com.vasche.validator.ValidationResult;

import java.util.List;
import java.util.Optional;

public class HallService {

    private final CreateHallValidator createHallValidator;

    private final HallRepository hallDao;

    private final CreateHallMapper createHallMapper;

    private final HallMapper hallMapper;

    public HallService() {
        this(new CreateHallValidator(),
                new HallRepository(),
                new CreateHallMapper(),
                new HallMapper());
    }

    public HallService(CreateHallValidator createHallValidator,
                        HallRepository hallDao,
                        CreateHallMapper createHallMapper,
                        HallMapper hallMapper) {
        this.createHallValidator = createHallValidator;
        this.hallDao = hallDao;
        this.createHallMapper = createHallMapper;
        this.hallMapper = hallMapper;
    }


    public Integer create(final CreateHallDto createHallDto) throws RepositoryException {
        final ValidationResult validationResult = createHallValidator.isValid(createHallDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        Hall hall = createHallMapper.mapFrom(createHallDto);
        return hallDao.save(hall).getId();
    }

    public void update(final CreateHallDto createHallDto) throws RepositoryException {
        final ValidationResult validationResult = createHallValidator.isValid(createHallDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        Hall hall = createHallMapper.mapFrom(createHallDto);
        hallDao.update(hall);
    }


    public boolean delete(final Integer hallId) throws RepositoryException {
        return hallDao.delete(hallId);
    }

    public Optional<HallDto> findById(final Integer hallId) throws RepositoryException {
        return hallDao.findById(hallId)
                .map(hallMapper::mapFrom);
    }

    public Optional<HallDto> findByLineId(final Integer lineId) throws RepositoryException {
        return hallDao.findByLineId(lineId)
                .map(hallMapper::mapFrom);
    }

    public Optional<HallDto> findByName(final String name) throws RepositoryException {
        return hallDao.findByName(name)
                .map(hallMapper::mapFrom);
    }

    public List<HallDto> findAll() throws RepositoryException {
        return hallDao.findAll().stream()
                .map(hallMapper::mapFrom)
                .toList();
    }

}
