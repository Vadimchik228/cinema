package com.vasche.service;

import com.vasche.dao.MovieDao;
import com.vasche.dto.filter.MovieFilterDto;
import com.vasche.dto.movie.CreateMovieDto;
import com.vasche.dto.movie.MovieDto;
import com.vasche.entity.Movie;
import com.vasche.exception.DaoException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.movie.CreateMovieMapper;
import com.vasche.mapper.movie.MovieMapper;
import com.vasche.validator.CreateMovieValidator;
import com.vasche.validator.ValidationResult;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.vasche.util.constants.FilteredAttributes.*;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class MovieService {

    private static final MovieService INSTANCE = new MovieService();

    private final MovieDao movieDao = MovieDao.getInstance();

    private final MovieMapper movieMapper = MovieMapper.getInstance();

    private final CreateMovieValidator createMovieValidator = CreateMovieValidator.getInstance();

    private final CreateMovieMapper createMovieMapper = CreateMovieMapper.getInstance();

    private final ImageService imageService = ImageService.getInstance();


    public static MovieService getInstance() {
        return INSTANCE;
    }

    public List<MovieDto> findAllByFilter(MovieFilterDto filter) throws DaoException {

        Map<String, Integer> mapOfAttributeAndNumber = new HashMap<>();
        mapOfAttributeAndNumber.put(TITLE, null);
        mapOfAttributeAndNumber.put(GENRE, null);
        mapOfAttributeAndNumber.put(MINIMUM_AGE, null);

        Map<String, Object> mapOfAttributeAndValue = new HashMap<>();
        mapOfAttributeAndValue.put(TITLE, filter.getTitle());
        mapOfAttributeAndValue.put(GENRE, filter.getGenre());
        mapOfAttributeAndValue.put(MINIMUM_AGE, filter.getMinimumAge());

        int i = 0;
        String condition = " WHERE ( ";
        if (filter.getTitle() != null) {
            condition += TITLE.concat(" LIKE concat(ltrim(?), '%') AND ");
            i++;
            mapOfAttributeAndNumber.put(TITLE, i);
        }
        if (filter.getGenre() != null) {
            condition += GENRE.concat(" LIKE ? AND ");
            i++;
            mapOfAttributeAndNumber.put(GENRE, i);
        }
        if (filter.getMinimumAge() != null) {
            condition += MINIMUM_AGE.concat("  = ?  AND ");
            i++;
            mapOfAttributeAndNumber.put(MINIMUM_AGE, i);
        }
        condition += " 1 = 1)";

        return movieDao.findAllByFilter(condition, mapOfAttributeAndNumber, mapOfAttributeAndValue)
                .stream()
                .map(movieMapper::mapFrom)
                .toList();
    }

    public List<MovieDto> findAll() throws DaoException {
        return movieDao.findAll().stream()
                .map(movieMapper::mapFrom)
                .toList();
    }

    public Optional<MovieDto> findById(final Integer movieId) throws DaoException {
        return movieDao.findById(movieId).map(movieMapper::mapFrom);
    }

    public Integer create(final CreateMovieDto createMovieDto) throws IOException, DaoException {
        final ValidationResult validationResult = createMovieValidator.isValid(createMovieDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        final Movie movie = createMovieMapper.mapFrom(createMovieDto);
        //imageService.upload(movie.getImageUrl(), createMovieDto.getImage().getInputStream());
        return movieDao.save(movie).getId();
    }

    public boolean delete(final Integer movieId) throws DaoException {
        return movieDao.delete(movieId);
    }

    public void update(final CreateMovieDto createMovieDto) throws DaoException, IOException {
        final ValidationResult validationResult = createMovieValidator.isValid(createMovieDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        final Movie movie = createMovieMapper.mapFrom(createMovieDto);
        imageService.upload(movie.getImageUrl(), createMovieDto.getImage().getInputStream());
        movieDao.update(movie);
    }
}
