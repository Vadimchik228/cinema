package com.vasche.repository;

import com.vasche.entity.Genre;
import com.vasche.entity.Movie;
import com.vasche.exception.RepositoryException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.vasche.constant.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


public class MovieRepositoryTest extends RepositoryTestBase {
    private final MovieRepository movieDao = new MovieRepository();

    @Test
    void save() throws RepositoryException {
        Movie movie = getMovie(MOVIE_TITLE1);
        Movie actualResult = movieDao.save(movie);

        assertNotNull(actualResult.getId());
    }

    @Test
    void findById() throws RepositoryException {

        Movie movie = movieDao.save(getMovie(MOVIE_TITLE1));
        Optional<Movie> actualResult = movieDao.findById(movie.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(movie);
    }

    @Test
    void findAll() throws RepositoryException {

        Movie movie1 = movieDao.save(getMovie(MOVIE_TITLE1));
        Movie movie2 = movieDao.save(getMovie(MOVIE_TITLE2));
        Movie movie3 = movieDao.save(getMovie(MOVIE_TITLE3));

        List<Movie> actualResult = movieDao.findAll();

        assertThat(actualResult).hasSize(3);
        List<Integer> moviesIds = actualResult.stream()
                .map(Movie::getId)
                .toList();
        assertThat(moviesIds).contains(movie1.getId(), movie2.getId(), movie3.getId());

    }

    @Test
    void findAllByFilter() throws RepositoryException {
        movieDao.save(MOVIE1);
        movieDao.save(MOVIE2);
        movieDao.save(MOVIE3);
        movieDao.save(MOVIE4);
        movieDao.save(MOVIE5);

        List<Movie> actualResult = movieDao.findAllByFilter(MOVIE_CONDITION,
                MOVIE_MAP_OF_ATTRIBUTE_AND_NUMBER, MOVIE_MAP_OF_ATTRIBUTE_AND_VALUE);

        assertThat(actualResult).isEqualTo(RESULT_LIST_OF_MOVIES);
    }

    @Test
    void shouldNotFindByIdIfMovieDoesNotExist() throws RepositoryException {
        movieDao.save(getMovie(MOVIE_TITLE1));
        Optional<Movie> actualResult = movieDao.findById(2000000);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void deleteExistingEntity() throws RepositoryException {
        Movie movie = movieDao.save(getMovie(MOVIE_TITLE1));
        boolean actualResult = movieDao.delete(movie.getId());

        assertTrue(actualResult);
    }

    @Test
    void deleteNotExistingEntity() throws RepositoryException {
        movieDao.save(getMovie(MOVIE_TITLE1));
        boolean actualResult = movieDao.delete(100500);
        assertFalse(actualResult);
    }

    @Test
    void update() throws RepositoryException {
        Movie movie = getMovie(MOVIE_TITLE1);
        movieDao.save(movie);

        movie.setDescription("Another one description");
        movie.setDurationMin(1000);
        movie.setMinimumAge(18);
        movie.setGenre(Genre.DRAMA);

        movieDao.update(movie);

        Movie updatedMovie = movieDao.findById(movie.getId()).get();
        assertThat(updatedMovie).isEqualTo(movie);
    }
}