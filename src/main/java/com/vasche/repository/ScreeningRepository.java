package com.vasche.repository;

import com.vasche.entity.Genre;
import com.vasche.entity.Movie;
import com.vasche.entity.Screening;
import com.vasche.exception.RepositoryException;
import com.vasche.util.ConnectionManager;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ScreeningRepository implements Repository<Integer, Screening> {

    private static final String DELETE_SQL = """
            DELETE from screenings
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO screenings (start_time, price, movie_id, hall_id) 
            VALUES (?, ?, ?, ?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE screenings
            SET start_time = ?, price = ?, movie_id = ?, hall_id = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id, start_time, price, movie_id, hall_id
            FROM screenings
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id, start_time, price, movie_id, hall_id
            FROM screenings
            WHERE id = ?
            """;

    private static final String FIND_ALL_DISTINCT_MOVIES_BY_FILTER_SQL = """
            SELECT DISTINCT m.id, m.title, m.description, m.duration_min, m.image_url, m.minimum_age, m.genre
            FROM screenings
            JOIN movies m ON m.id = screenings.movie_id
            """;

    private static final String FIND_ALL_BY_MOVIE_ID_SQL = """
            SELECT id, start_time, price, movie_id, hall_id
            FROM screenings
            WHERE movie_id = ?
            """;

    private static final String FIND_ALL_BY_USER_ID_SQL = """
            SELECT s.id, s.start_time, s.price, s.movie_id, s.hall_id
            FROM screenings s
            JOIN reservations r on s.id = r.screening_id
            JOIN users u on u.id = r.user_id
            WHERE u.id = ?
            """;

    private static final String FIND_ALL_AVAILABLE_BY_MOVIE_ID = """
            SELECT id, start_time, price, movie_id, hall_id
            FROM screenings sc
            WHERE sc.movie_id = ? AND ((SELECT count(s.id)
                                        FROM reservations r
                                        JOIN seats s on s.id = r.seat_id
                                        WHERE r.screening_id = sc.id) < (SELECT count(s2.id)
                                                                         FROM seats s2
                                                                         JOIN lines l on l.id = s2.line_id
                                                                         JOIN halls h on h.id = l.hall_id
                                                                         WHERE h.id = sc.hall_id))    
            """;

    private static final String FIND_BY_RESERVATION_ID_SQl = """
            SELECT s.id, s.start_time, s.price, s.movie_id, s.hall_id
            FROM screenings s
            JOIN reservations r on s.id = r.screening_id
            WHERE r.id = ?
            """;

    public ScreeningRepository() {
    }

    @Override
    public boolean delete(Integer id) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't delete screening from Database");
        }
    }

    @Override
    public Screening save(Screening screening) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(screening.getStartTime()));
            preparedStatement.setBigDecimal(2, screening.getPrice());
            preparedStatement.setInt(3, screening.getMovieId());
            preparedStatement.setInt(4, screening.getHallId());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                screening.setId(generatedKeys.getInt("id"));
            }
            return screening;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Screening screening) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(screening.getStartTime()));
            preparedStatement.setBigDecimal(2, screening.getPrice());
            preparedStatement.setInt(3, screening.getMovieId());
            preparedStatement.setInt(4, screening.getHallId());
            preparedStatement.setInt(5, screening.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't update screening in Database");
        }
    }

    @Override
    public Optional<Screening> findById(Integer id) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            Screening screening = null;
            if (resultSet.next()) {
                screening = buildScreening(resultSet);
            }
            return Optional.ofNullable(screening);
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get screening by id from Database");
        }
    }

    @Override
    public List<Screening> findAll() throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Screening> screenings = new ArrayList<>();
            while (resultSet.next()) {
                screenings.add(buildScreening(resultSet));
            }
            return screenings;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get list of screenings from Database");
        }
    }

    public List<Screening> findAllByMovieId(Integer id) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_BY_MOVIE_ID_SQL)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            List<Screening> screenings = new ArrayList<>();
            while (resultSet.next()) {
                screenings.add(buildScreening(resultSet));
            }
            return screenings;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get list of screenings from Database");
        }
    }

    public List<Screening> findAllAvailableByMovieId(Integer id) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_AVAILABLE_BY_MOVIE_ID)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            List<Screening> screenings = new ArrayList<>();
            while (resultSet.next()) {
                screenings.add(buildScreening(resultSet));
            }
            return screenings;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get list of available screenings by movieId from Database");
        }
    }

    public List<Screening> findAllByUserId(Integer userId) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_BY_USER_ID_SQL)) {
            preparedStatement.setInt(1, userId);
            var resultSet = preparedStatement.executeQuery();
            List<Screening> screenings = new ArrayList<>();
            while (resultSet.next()) {
                screenings.add(buildScreening(resultSet));
            }
            return screenings;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get list of screenings by userId from Database");
        }
    }

    public Optional<Screening> findByReservationId(Integer reservationId) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_RESERVATION_ID_SQl)) {
            preparedStatement.setInt(1, reservationId);
            var resultSet = preparedStatement.executeQuery();
            Screening screening = null;
            if (resultSet.next()) {
                screening = buildScreening(resultSet);
            }
            return Optional.ofNullable(screening);
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get screening by reservationId from Database");
        }
    }

    public List<Movie> findAllDistinctMoviesByFilter(String condition, Map<String, Integer> mapOfAttributeAndNumber,
                                                     Map<String, Object> mapOfAttributeAndValue) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_DISTINCT_MOVIES_BY_FILTER_SQL.concat(condition))) {
            if (mapOfAttributeAndNumber.get("genre") != null) {
                preparedStatement.setString(mapOfAttributeAndNumber.get("genre"), mapOfAttributeAndValue.get("genre").toString());
            }
            if (mapOfAttributeAndNumber.get("title") != null) {
                preparedStatement.setString(mapOfAttributeAndNumber.get("title"), mapOfAttributeAndValue.get("title").toString());
            }
            if (mapOfAttributeAndNumber.get("date") != null) {
                preparedStatement.setDate(mapOfAttributeAndNumber.get("date"), Date.valueOf((LocalDate) mapOfAttributeAndValue.get("date")));
            }

            var resultSet = preparedStatement.executeQuery();

            List<Movie> movies = new ArrayList<>();
            while (resultSet.next()) {
                movies.add(buildMovie(resultSet));
            }
            return movies;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get list of movies by filter from Database");
        }
    }

    private static Screening buildScreening(ResultSet resultSet) throws RepositoryException {
        try {
            return Screening.builder()
                    .id(resultSet.getInt("id"))
                    .startTime(resultSet.getTimestamp("start_time").toLocalDateTime())
                    .price(resultSet.getObject("price", BigDecimal.class))
                    .movieId(resultSet.getInt("movie_id"))
                    .hallId(resultSet.getInt("hall_id"))
                    .build();
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't build screening");
        }
    }

    private static Movie buildMovie(ResultSet resultSet) throws RepositoryException {
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