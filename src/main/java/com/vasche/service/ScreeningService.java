package com.vasche.service;

import com.vasche.repository.ScreeningRepository;
import com.vasche.dto.filter.ScreeningFilterDto;
import com.vasche.dto.movie.MovieDto;
import com.vasche.dto.screening.CreateScreeningDto;
import com.vasche.dto.screening.ScreeningDto;
import com.vasche.entity.Screening;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.movie.MovieMapper;
import com.vasche.mapper.screening.CreateScreeningMapper;
import com.vasche.mapper.screening.ScreeningMapper;
import com.vasche.validator.CreateScreeningValidator;
import com.vasche.validator.ValidationResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.vasche.util.constants.FilteredAttributes.*;


public class ScreeningService {
    private final ScreeningRepository screeningDao;
    private final MovieMapper movieMapper;
    private final ScreeningMapper screeningMapper;
    private final CreateScreeningValidator createScreeningValidator;
    private final CreateScreeningMapper createScreeningMapper;

    public ScreeningService() {
        this(new CreateScreeningValidator(),
                new ScreeningRepository(),
                new CreateScreeningMapper(),
                new ScreeningMapper(),
                new MovieMapper());
    }

    public ScreeningService(CreateScreeningValidator createScreeningValidator,
                            ScreeningRepository screeningDao,
                            CreateScreeningMapper createScreeningMapper,
                            ScreeningMapper screeningMapper,
                            MovieMapper movieMapper) {
        this.createScreeningValidator = createScreeningValidator;
        this.screeningDao = screeningDao;
        this.createScreeningMapper = createScreeningMapper;
        this.screeningMapper = screeningMapper;
        this.movieMapper = movieMapper;
    }

    public List<MovieDto> findAllDistinctMoviesByFilter(ScreeningFilterDto filter) throws RepositoryException {

        Map<String, Integer> mapOfAttributeAndNumber = new HashMap<>();
        mapOfAttributeAndNumber.put(TITLE, null);
        mapOfAttributeAndNumber.put(GENRE, null);
        mapOfAttributeAndNumber.put(DATE, null);

        Map<String, Object> mapOfAttributeAndValue = new HashMap<>();
        mapOfAttributeAndValue.put(TITLE, filter.getTitle());
        mapOfAttributeAndValue.put(GENRE, filter.getGenre());
        mapOfAttributeAndValue.put(DATE, filter.getDate());

        int i = 0;
        String condition = " WHERE ( ";
        if (filter.getTitle() != null) {
            condition += " m." + TITLE + " LIKE concat(ltrim(?), '%') AND ";
            i++;
            mapOfAttributeAndNumber.put(TITLE, i);
        }
        if (filter.getGenre() != null) {
            condition += " m." + GENRE + " LIKE ? AND ";
            i++;
            mapOfAttributeAndNumber.put(GENRE, i);
        }
        if (filter.getDate() != null) {
            condition += " DATE(start_time) = ?  AND ";
            i++;
            mapOfAttributeAndNumber.put(DATE, i);
        }
        condition += " 1 = 1)";

        return screeningDao.findAllDistinctMoviesByFilter(condition, mapOfAttributeAndNumber, mapOfAttributeAndValue)
                .stream()
                .map(movieMapper::mapFrom)
                .toList();
    }

    public List<ScreeningDto> findAll() throws RepositoryException {
        return screeningDao.findAll().stream()
                .map(screeningMapper::mapFrom)
                .toList();
    }

    public List<ScreeningDto> findAllByMovieId(final Integer movieId) throws RepositoryException {
        return screeningDao.findAllByMovieId(movieId).stream()
                .map(screeningMapper::mapFrom)
                .toList();
    }

    public List<ScreeningDto> findAllByUserId(final Integer userId) throws RepositoryException {
        return screeningDao.findAllByUserId(userId).stream()
                .map(screeningMapper::mapFrom)
                .toList();
    }

    public List<ScreeningDto> findAllAvailable(final Integer movieId) throws RepositoryException {
        return screeningDao.findAllAvailableByMovieId(movieId).stream()
                .map(screeningMapper::mapFrom)
                .toList();
    }

    public Optional<ScreeningDto> findById(final Integer screeningId) throws RepositoryException {
        return screeningDao.findById(screeningId).map(screeningMapper::mapFrom);
    }

    public Optional<ScreeningDto> findByReservationId(final Integer reservationId) throws RepositoryException {
        return screeningDao.findByReservationId(reservationId).map(screeningMapper::mapFrom);
    }

    public Integer create(final CreateScreeningDto createScreeningDto) throws RepositoryException {
        final ValidationResult validationResult = createScreeningValidator.isValid(createScreeningDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        final Screening screening = createScreeningMapper.mapFrom(createScreeningDto);
        return screeningDao.save(screening).getId();
    }

    public void update(final CreateScreeningDto createScreeningDto) throws RepositoryException {
        final ValidationResult validationResult = createScreeningValidator.isValid(createScreeningDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        final Screening screening = createScreeningMapper.mapFrom(createScreeningDto);
        screeningDao.update(screening);
    }

    public boolean delete(final Integer id) throws RepositoryException {
        return screeningDao.delete(id);
    }
}
