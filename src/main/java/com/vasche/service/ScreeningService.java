package com.vasche.service;

import com.vasche.dto.filter.ScreeningFilterDto;
import com.vasche.dto.movie.MovieDto;
import com.vasche.dto.screening.CreateScreeningDto;
import com.vasche.dto.screening.ScreeningAllDataDto;
import com.vasche.dto.screening.ScreeningDto;
import com.vasche.dto.screening.ScreeningWithHallDto;
import com.vasche.entity.Genre;
import com.vasche.entity.Hall;
import com.vasche.entity.Movie;
import com.vasche.entity.Screening;
import com.vasche.exception.RepositoryException;
import com.vasche.exception.ScreeningServiceException;
import com.vasche.exception.ServiceException;
import com.vasche.exception.ValidationException;
import com.vasche.mapper.movie.MovieMapper;
import com.vasche.mapper.screening.CreateScreeningMapper;
import com.vasche.mapper.screening.ScreeningAllDataMapper;
import com.vasche.mapper.screening.ScreeningMapper;
import com.vasche.mapper.screening.ScreeningWithHallMapper;
import com.vasche.repository.HallRepository;
import com.vasche.repository.MovieRepository;
import com.vasche.repository.ScreeningRepository;
import com.vasche.validator.CreateScreeningValidator;
import com.vasche.validator.ValidationResult;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.vasche.util.constants.FilteredAttributes.*;


public class ScreeningService {
    private static final Logger LOGGER = Logger.getLogger(ScreeningService.class);
    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;
    private final MovieMapper movieMapper;
    private final ScreeningMapper screeningMapper;
    private final CreateScreeningValidator createScreeningValidator;
    private final CreateScreeningMapper createScreeningMapper;
    private final ScreeningAllDataMapper screeningAllDataMapper;
    private final ScreeningWithHallMapper screeningWithHallMapper;

    public ScreeningService() {
        this(new CreateScreeningValidator(),
                new ScreeningRepository(),
                new MovieRepository(),
                new HallRepository(),
                new CreateScreeningMapper(),
                new ScreeningMapper(),
                new MovieMapper(),
                new ScreeningAllDataMapper(),
                new ScreeningWithHallMapper());
    }

    public ScreeningService(CreateScreeningValidator createScreeningValidator,
                            ScreeningRepository screeningRepository,
                            MovieRepository movieRepository,
                            HallRepository hallRepository,
                            CreateScreeningMapper createScreeningMapper,
                            ScreeningMapper screeningMapper,
                            MovieMapper movieMapper,
                            ScreeningAllDataMapper screeningAllDataMapper,
                            ScreeningWithHallMapper screeningWithHallMapper) {
        this.createScreeningValidator = createScreeningValidator;
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.hallRepository = hallRepository;
        this.createScreeningMapper = createScreeningMapper;
        this.screeningMapper = screeningMapper;
        this.movieMapper = movieMapper;
        this.screeningAllDataMapper = screeningAllDataMapper;
        this.screeningWithHallMapper = screeningWithHallMapper;
    }

    public Integer create(final CreateScreeningDto createScreeningDto) throws ServiceException {
        final ValidationResult validationResult = createScreeningValidator.isValid(createScreeningDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        final Screening screening = createScreeningMapper.mapFrom(createScreeningDto);
        try {
            return screeningRepository.save(screening).getId();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't save new screening", e);
        }
    }

    public Optional<ScreeningDto> findById(final Integer screeningId) throws ServiceException {
        try {
            return screeningRepository.findById(screeningId).map(screeningMapper::mapFrom);
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get screeningDto by id", e);
        }
    }

    public Optional<ScreeningAllDataDto> findWithAllDataById(final Integer screeningId) throws ServiceException {
        try {
            return screeningRepository.findById(screeningId)
                    .map(screening -> {
                        Optional<Movie> movie;
                        Optional<Hall> hall;
                        try {
                            movie = movieRepository.findById(screening.getMovieId());
                            hall = hallRepository.findById(screening.getHallId());
                        } catch (RepositoryException e) {
                            LOGGER.error(e.getMessage());
                            throw new ScreeningServiceException("Couldn't get screeningDto with all data by id", e);
                        }
                        if (movie.isPresent() && hall.isPresent()) {
                            return screeningAllDataMapper.mapFrom(screening, movie.get(), hall.get());
                        }
                        return null;
                    });
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get screeningDtos with all data by id", e);
        }
    }

    public Optional<ScreeningDto> findByReservationId(final Integer reservationId) throws ServiceException {
        try {
            return screeningRepository.findByReservationId(reservationId).map(screeningMapper::mapFrom);
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get screeningDto by reservationId", e);
        }
    }

    public List<ScreeningDto> findAll() throws ServiceException {
        try {
            return screeningRepository.findAll().stream()
                    .map(screeningMapper::mapFrom)
                    .toList();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get all screeningDtos", e);
        }
    }

    public List<ScreeningWithHallDto> findAllAvailable(final Integer movieId) throws ServiceException {
        try {
            return screeningRepository.findAllAvailableByMovieId(movieId).stream()
                    .map(screening -> {
                        Optional<Hall> hall;
                        try {
                            hall = hallRepository.findById(screening.getHallId());
                        } catch (RepositoryException e) {
                            LOGGER.error(e.getMessage());
                            throw new ScreeningServiceException("Couldn't get available screeningDto with hall by movieId", e);
                        }
                        return hall.map(value -> screeningWithHallMapper.mapFrom(screening, value)).orElse(null);
                    })
                    .toList();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get all available screeningDtos with hall by movieId", e);
        }
    }

    public List<MovieDto> findAllDisplayedMoviesByFilter(final ScreeningFilterDto filter) throws ServiceException {
        Map<String, Integer> mapOfAttributeAndNumber = new HashMap<>();
        mapOfAttributeAndNumber.put(TITLE, null);
        mapOfAttributeAndNumber.put(GENRE, null);
        mapOfAttributeAndNumber.put(DATE, null);

        try {
            return screeningRepository.findAllDisplayedMoviesByFilter(mapOfAttributeAndNumber,
                            createMapOfAttributeAndValueForAllDisplayedMovies(filter))
                    .stream()
                    .map(movieMapper::mapFrom)
                    .toList();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get all displayed movies by filter", e);
        }
    }

    private Map<String, Object> createMapOfAttributeAndValueForAllDisplayedMovies(final ScreeningFilterDto filter) {
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
        if (filter.getDate() != null && !filter.getDate().isEmpty()) {
            mapOfAttributeAndValue.put(DATE, filter.getDate());
        } else {
            mapOfAttributeAndValue.put(DATE, null);
        }
        return mapOfAttributeAndValue;
    }

    public List<ScreeningAllDataDto> findAllWithAllDataByFilter(final ScreeningFilterDto filter) throws ServiceException {

        Map<String, Integer> mapOfAttributeAndNumber = new HashMap<>();
        mapOfAttributeAndNumber.put(TITLE, null);
        mapOfAttributeAndNumber.put(GENRE, null);
        mapOfAttributeAndNumber.put(DATE, null);

        try {
            return screeningRepository.findAllByFilter(mapOfAttributeAndNumber,
                            createMapOfAttributeAndValueForAllScreenings(filter))
                    .stream()
                    .map(screening -> {
                        Optional<Movie> movie;
                        Optional<Hall> hall;
                        try {
                            movie = movieRepository.findById(screening.getMovieId());
                            hall = hallRepository.findById(screening.getHallId());
                        } catch (RepositoryException e) {
                            LOGGER.error(e.getMessage());
                            throw new ScreeningServiceException("Couldn't get screeningDto with all data by filter", e);
                        }
                        if (movie.isPresent() && hall.isPresent()) {
                            return screeningAllDataMapper.mapFrom(screening, movie.get(), hall.get());
                        }
                        return null;
                    })
                    .toList();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't get screeningDtos with all data by filter", e);
        }
    }

    Map<String, Object> createMapOfAttributeAndValueForAllScreenings(final ScreeningFilterDto filter) {
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
        if (filter.getDate() != null && !filter.getDate().isEmpty()) {
            mapOfAttributeAndValue.put(DATE, filter.getDate());
        } else {
            mapOfAttributeAndValue.put(DATE, null);
        }
        return mapOfAttributeAndValue;
    }

    public void update(final CreateScreeningDto createScreeningDto) throws ServiceException {
        final ValidationResult validationResult = createScreeningValidator.isValid(createScreeningDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        final Screening screening = createScreeningMapper.mapFrom(createScreeningDto);
        try {
            screeningRepository.update(screening);
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't update screening", e);
        }
    }

    public boolean delete(final Integer id) throws ServiceException {
        try {
            return screeningRepository.delete(id);
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't delete screening by id", e);
        }
    }

    public Integer countNumberOfScreenings() throws ServiceException {
        try {
            return screeningRepository.findAll().size();
        } catch (RepositoryException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Couldn't count number of screenings", e);
        }
    }
}
