package com.vasche.service;

import com.vasche.repository.MovieRepository;
import com.vasche.dto.filter.MovieFilterDto;
import com.vasche.dto.movie.CreateMovieDto;
import com.vasche.dto.movie.MovieDto;
import com.vasche.entity.Movie;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.movie.CreateMovieMapper;
import com.vasche.mapper.movie.MovieMapper;
import com.vasche.validator.CreateMovieValidator;
import com.vasche.validator.ValidationResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.vasche.util.constants.FilteredAttributes.*;

public class MovieService {

    private MovieRepository movieDao;

    private MovieMapper movieMapper;

    private CreateMovieValidator createMovieValidator;

    private CreateMovieMapper createMovieMapper;

    private ImageService imageService;


    public MovieService() {
        movieDao = new MovieRepository();
        movieMapper = new MovieMapper();
        createMovieValidator = new CreateMovieValidator();
        createMovieMapper = new CreateMovieMapper();
        imageService = new ImageService();
    }

    public MovieService(MovieRepository movieDao,
                        MovieMapper movieMapper,
                        CreateMovieValidator createMovieValidator,
                        CreateMovieMapper createMovieMapper,
                        ImageService imageService) {
        this.movieDao = movieDao;
        this.movieMapper = movieMapper;
        this.createMovieMapper = createMovieMapper;
        this.createMovieValidator = createMovieValidator;
        this.imageService = imageService;
    }

    public List<MovieDto> findAllByFilter(MovieFilterDto filter) throws RepositoryException {

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

    public List<MovieDto> findAll() throws RepositoryException {
        return movieDao.findAll().stream()
                .map(movieMapper::mapFrom)
                .toList();
    }

    public Optional<MovieDto> findById(final Integer movieId) throws RepositoryException {
        return movieDao.findById(movieId).map(movieMapper::mapFrom);
    }

    public Integer create(final CreateMovieDto createMovieDto) throws RepositoryException, IOException {
        final ValidationResult validationResult = createMovieValidator.isValid(createMovieDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        final Movie movie = createMovieMapper.mapFrom(createMovieDto);
        if (createMovieDto.getImage() != null) {
            imageService.upload(movie.getImageUrl(), createMovieDto.getImage().getInputStream());
        }
        return movieDao.save(movie).getId();
    }

    public void update(final CreateMovieDto createMovieDto) throws RepositoryException, IOException {
        final ValidationResult validationResult = createMovieValidator.isValid(createMovieDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        final Movie movie = createMovieMapper.mapFrom(createMovieDto);
        if (createMovieDto.getImage() != null) {
            imageService.upload(movie.getImageUrl(), createMovieDto.getImage().getInputStream());
        }
        movieDao.update(movie);
    }

    public boolean delete(final Integer movieId) throws RepositoryException {
        return movieDao.delete(movieId);
    }
}
