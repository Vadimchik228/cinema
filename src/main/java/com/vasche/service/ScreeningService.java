package com.vasche.service;

import com.vasche.dao.ScreeningDao;
import com.vasche.dto.filter.ScreeningFilterDto;
import com.vasche.dto.movie.MovieDto;
import com.vasche.dto.screening.CreateScreeningDto;
import com.vasche.dto.screening.ScreeningDto;
import com.vasche.entity.Screening;
import com.vasche.exception.DaoException;
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
    private static final ScreeningService INSTANCE = new ScreeningService();

    private final ScreeningDao screeningDao = ScreeningDao.getInstance();
    private final MovieMapper movieMapper = MovieMapper.getInstance();
    private final ScreeningMapper screeningMapper = ScreeningMapper.getInstance();
    private final CreateScreeningValidator createScreeningValidator = CreateScreeningValidator.getInstance();
    private final CreateScreeningMapper createScreeningMapper = CreateScreeningMapper.getInstance();

    private ScreeningService() {
    }

    public static ScreeningService getInstance() {
        return INSTANCE;
    }

    public List<MovieDto> findAllDistinctMoviesByFilter(ScreeningFilterDto filter) throws DaoException {

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

    public List<ScreeningDto> findAll() throws DaoException {
        return screeningDao.findAll().stream()
                .map(screeningMapper::mapFrom)
                .toList();
    }

    public List<ScreeningDto> findAllByMovieId(final Integer movieId) throws DaoException {
        return screeningDao.findAllByMovieId(movieId).stream()
                .map(screeningMapper::mapFrom)
                .toList();
    }

    public Optional<ScreeningDto> findById(final Integer id) throws DaoException {
        return screeningDao.findById(id).map(screeningMapper::mapFrom);
    }

    public Integer create(final CreateScreeningDto createScreeningDto) throws DaoException {
        final ValidationResult validationResult = createScreeningValidator.isValid(createScreeningDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        final Screening screening = createScreeningMapper.mapFrom(createScreeningDto);
        return screeningDao.save(screening).getId();
    }

    public void update(final CreateScreeningDto createScreeningDto) throws DaoException {
        final ValidationResult validationResult = createScreeningValidator.isValid(createScreeningDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        final Screening screening = createScreeningMapper.mapFrom(createScreeningDto);
        screeningDao.update(screening);
    }

    public boolean delete(final Integer id) throws DaoException {
        return screeningDao.delete(id);
    }
}
