package com.vasche.service;

import com.vasche.dto.filter.MovieFilterDto;
import com.vasche.dto.movie.CreateMovieDto;
import com.vasche.dto.movie.MovieDto;
import com.vasche.entity.Genre;
import com.vasche.entity.Movie;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ServiceException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.movie.CreateMovieMapper;
import com.vasche.mapper.movie.MovieMapper;
import com.vasche.repository.MovieRepository;
import com.vasche.util.NumericUtil;
import com.vasche.validator.CreateMovieValidator;
import com.vasche.validator.ValidationResult;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.vasche.util.constants.FilteredAttributes.*;

public class MovieService {
    private static final Logger LOGGER = Logger.getLogger(MovieService.class);
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final CreateMovieMapper createMovieMapper;
    private final CreateMovieValidator createMovieValidator;
    private final ImageService imageService;

    public MovieService() {
        this(new MovieRepository(),
                new MovieMapper(),
                new CreateMovieMapper(),
                new CreateMovieValidator(),
                new ImageService());
    }

    public MovieService(MovieRepository movieRepository,
                        MovieMapper movieMapper,
                        CreateMovieMapper createMovieMapper,
                        CreateMovieValidator createMovieValidator,
                        ImageService imageService) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.createMovieMapper = createMovieMapper;
        this.createMovieValidator = createMovieValidator;
        this.imageService = imageService;
    }

    public Integer create(final CreateMovieDto createMovieDto) throws ServiceException {
        final ValidationResult validationResult = createMovieValidator.isValid(createMovieDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        final Movie movie = createMovieMapper.mapFrom(createMovieDto);
        if (createMovieDto.getImage() != null) {
            try {
                imageService.upload(movie.getImageUrl(), createMovieDto.getImage().getInputStream());
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
                throw new ServiceException("Couldn't upload movie's image while creating", e);
            }
        }
        try {
            return movieRepository.save(movie).getId();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't save new movie", e);
        }
    }

    public Optional<MovieDto> findById(final Integer movieId) throws ServiceException {
        try {
            return movieRepository.findById(movieId).map(movieMapper::mapFrom);
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get movieDto by id", e);
        }
    }

    public List<MovieDto> findAll() throws ServiceException {
        try {
            return movieRepository.findAll().stream()
                    .map(movieMapper::mapFrom)
                    .toList();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't find all movieDtos", e);
        }
    }

    public List<MovieDto> findAllByFilter(MovieFilterDto filter) throws ServiceException {

        Map<String, Integer> mapOfAttributeAndNumber = new HashMap<>();
        mapOfAttributeAndNumber.put(TITLE, null);
        mapOfAttributeAndNumber.put(GENRE, null);
        mapOfAttributeAndNumber.put(MINIMUM_AGE, null);

        Map<String, Object> mapOfAttributeAndValue = new HashMap<>();
        if (filter.getTitle() != null && !filter.getTitle().isEmpty()) {
            mapOfAttributeAndValue.put(TITLE, filter.getTitle());
        } else {
            mapOfAttributeAndValue.put(TITLE, null);
        }
        if (filter.getGenre() != null && !filter.getGenre().equals(Genre.ALL.name())) {
            mapOfAttributeAndValue.put(GENRE, filter.getGenre());
        } else {
            mapOfAttributeAndValue.put(GENRE, null);
        }
        if (NumericUtil.isMinimumAge(filter.getMinimumAge())) {
            mapOfAttributeAndValue.put(MINIMUM_AGE, filter.getMinimumAge());
        } else {
            mapOfAttributeAndValue.put(MINIMUM_AGE, null);
        }

        try {
            return movieRepository.findAllByFilter(mapOfAttributeAndNumber, mapOfAttributeAndValue)
                    .stream()
                    .map(movieMapper::mapFrom)
                    .toList();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't find all movieDtos by filter", e);
        }
    }

    public void update(final CreateMovieDto createMovieDto) throws ServiceException {
        final ValidationResult validationResult = createMovieValidator.isValid(createMovieDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        final Movie movie = createMovieMapper.mapFrom(createMovieDto);
        if (createMovieDto.getImage() != null) {
            try {
                imageService.upload(movie.getImageUrl(), createMovieDto.getImage().getInputStream());
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
                throw new ServiceException("Couldn't upload movie's image while updating", e);
            }
        }
        try {
            movieRepository.update(movie);
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't update movie", e);
        }
    }

    public boolean delete(final Integer movieId) throws ServiceException {
        try {
            return movieRepository.delete(movieId);
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't delete movie by movieID", e);
        }
    }

    public Integer countNumberOfMovies() throws ServiceException {
        try {
            return movieRepository.findAll().size();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't count number of movies", e);
        }
    }
}
