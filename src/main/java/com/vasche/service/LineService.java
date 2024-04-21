package com.vasche.service;

import com.vasche.repository.LineRepository;
import com.vasche.dto.line.CreateLineDto;
import com.vasche.dto.line.LineDto;
import com.vasche.entity.Line;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.line.CreateLineMapper;
import com.vasche.mapper.line.LineMapper;
import com.vasche.validator.CreateLineValidator;
import com.vasche.validator.ValidationResult;

import java.util.List;
import java.util.Optional;

public class LineService {

    private CreateLineValidator createLineValidator;

    private LineRepository lineDao;

    private CreateLineMapper createLineMapper;

    private LineMapper lineMapper;

    private LineService() {
        this(new CreateLineValidator(),
                new LineRepository(),
                new CreateLineMapper(),
                new LineMapper());
    }

    private LineService(CreateLineValidator createLineValidator,
                        LineRepository lineDao,
                        CreateLineMapper createLineMapper,
                        LineMapper lineMapper) {
        this.createLineValidator = createLineValidator;
        this.lineDao = lineDao;
        this.createLineMapper = createLineMapper;
        this.lineMapper = lineMapper;
    }


    public Integer create(final CreateLineDto createLineDto) throws RepositoryException {
        final ValidationResult validationResult = createLineValidator.isValid(createLineDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        Line line = createLineMapper.mapFrom(createLineDto);
        return lineDao.save(line).getId();
    }

    public void update(final CreateLineDto createLineDto) throws RepositoryException {
        final ValidationResult validationResult = createLineValidator.isValid(createLineDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        Line line = createLineMapper.mapFrom(createLineDto);
        lineDao.update(line);
    }

    public boolean delete(final Integer lineId) throws RepositoryException {
        return lineDao.delete(lineId);
    }

    public Optional<LineDto> findById(final Integer lineId) throws RepositoryException {
        return lineDao.findById(lineId)
                .map(lineMapper::mapFrom);
    }

    public Optional<LineDto> findBySeatId(final Integer seatId) throws RepositoryException {
        return lineDao.findBySeatId(seatId)
                .map(lineMapper::mapFrom);
    }

    public List<LineDto> findAll() throws RepositoryException {
        return lineDao.findAll().stream()
                .map(lineMapper::mapFrom)
                .toList();
    }

    public List<LineDto> findAllByHallId(final Integer hallId) throws RepositoryException {
        return lineDao.findAllByHallId(hallId).stream()
                .map(lineMapper::mapFrom)
                .toList();
    }
}
