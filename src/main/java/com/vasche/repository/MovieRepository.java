package com.vasche.repository;

import com.vasche.entity.Genre;
import com.vasche.entity.Movie;
import com.vasche.exception.RepositoryException;
import com.vasche.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MovieRepository implements Repository<Integer, Movie> {
    private static final String DELETE_SQL = """
            DELETE from movies
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO movies (title, description, duration_min, minimum_age, image_url, genre) 
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE movies
            SET title = ?, description = ?, duration_min = ?, minimum_age = ?, image_url = ?, genre = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id, title, description, duration_min, minimum_age, image_url, genre
            FROM movies 
            """;

    private static final String FIND_ALL_BY_ID_SQL = """
            SELECT id, title, description, duration_min, minimum_age, image_url, genre
            FROM movies
            WHERE id = ?
            """;

    public MovieRepository() {
    }

    @Override
    public boolean delete(Integer id) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't delete movie from Database");
        }
    }

    @Override
    public Movie save(Movie movie) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getDescription());
            preparedStatement.setInt(3, movie.getDurationMin());
            preparedStatement.setInt(4, movie.getMinimumAge());
            preparedStatement.setString(5, movie.getImageUrl());
            preparedStatement.setString(6, movie.getGenre().name());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                movie.setId(generatedKeys.getInt("id"));
            }
            return movie;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't save movie in Database");
        }
    }

    @Override
    public void update(Movie movie) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getDescription());
            preparedStatement.setInt(3, movie.getDurationMin());
            preparedStatement.setInt(4, movie.getMinimumAge());
            preparedStatement.setString(5, movie.getImageUrl());
            preparedStatement.setString(6, movie.getGenre().name());
            preparedStatement.setInt(7, movie.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't update movie in Database");
        }
    }

    @Override
    public Optional<Movie> findById(Integer id) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            Movie movie = null;
            if (resultSet.next()) {
                movie = buildEntity(resultSet);
            }
            return Optional.ofNullable(movie);
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get movie by id from Database");
        }
    }

    @Override
    public List<Movie> findAll() throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Movie> movies = new ArrayList<>();
            while (resultSet.next()) {
                movies.add(buildEntity(resultSet));
            }
            return movies;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get list of movies from Database");
        }
    }

    public List<Movie> findAllByFilter(String condition, Map<String, Integer> mapOfAttributeAndNumber,
                                       Map<String, Object> mapOfAttributeAndValue) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL.concat(condition))) {

            if (mapOfAttributeAndNumber.get("title") != null) {
                preparedStatement.setString(mapOfAttributeAndNumber.get("title"), mapOfAttributeAndValue.get("title").toString());
            }
            if (mapOfAttributeAndNumber.get("genre") != null) {
                preparedStatement.setString(mapOfAttributeAndNumber.get("genre"), mapOfAttributeAndValue.get("genre").toString());
            }
            if (mapOfAttributeAndNumber.get("minimum_age") != null) {
                preparedStatement.setInt(mapOfAttributeAndNumber.get("minimum_age"), (Integer) mapOfAttributeAndValue.get("minimum_age"));
            }

            var resultSet = preparedStatement.executeQuery();
            List<Movie> movies = new ArrayList<>();
            while (resultSet.next()) {
                movies.add(buildEntity(resultSet));
            }
            return movies;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get list of movies by filter from Database");
        }
    }

    private static Movie buildEntity(ResultSet resultSet) throws RepositoryException {
        try {
            return Movie.builder()
                    .id(resultSet.getInt("id"))
                    .title(resultSet.getObject("title", String.class))
                    .description(resultSet.getObject("description", String.class))
                    .durationMin(resultSet.getInt("duration_min"))
                    .minimumAge(resultSet.getInt("minimum_age"))
                    .imageUrl(resultSet.getObject("image_url", String.class))
                    .genre(Genre.valueOf(resultSet.getObject("genre", String.class)))
                    .build();
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't build movie");
        }
    }
}
