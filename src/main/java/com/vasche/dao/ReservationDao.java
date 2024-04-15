package com.vasche.dao;

import com.vasche.entity.Reservation;
import com.vasche.exception.DaoException;
import com.vasche.util.ConnectionManager;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ReservationDao implements Dao<Integer, Reservation> {
    private static final ReservationDao INSTANCE = new ReservationDao();
    private static final String DELETE_SQL = """
            DELETE from reservations
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO reservations (user_id, screening_id, seat_id) 
            VALUES (?, ?, ?)
            """;

    private static final String FIND_ALL_SQL = """
            SELECT *
            FROM reservations
            """;

    private static final String FIND_ALL_BY_USER_ID_SQL = """
            SELECT *
            FROM reservations r
            JOIN screenings s on s.id = r.screening_id
            WHERE user_id = ?
            ORDER BY s.start_time
            """;

    private static final String FIND_ALL_BY_SCREENING_ID_SQL = """
            SELECT *
            FROM reservations 
            WHERE screening_id = ?
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT *
            FROM reservations
            WHERE id = ?
            """;

    public static ReservationDao getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Couldn't delete reservation from Database");
        }
    }

    @Override
    public Reservation save(Reservation reservation) throws DaoException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, reservation.getUserId());
            preparedStatement.setInt(2, reservation.getScreeningId());
            preparedStatement.setInt(3, reservation.getSeatId());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                reservation.setId(generatedKeys.getInt("id"));
            }
            return reservation;
        } catch (SQLException e) {
            throw new DaoException("Couldn't save reservation in Database");
        }
    }

    @Override
    public void update(Reservation entity) throws DaoException {

    }

    @Override
    public Optional<Reservation> findById(Integer id) throws DaoException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            Reservation reservation = null;
            if (resultSet.next()) {
                reservation = buildEntity(resultSet);
            }
            return Optional.ofNullable(reservation);
        } catch (SQLException e) {
            throw new DaoException("Couldn't get reservation by id from Database");
        }
    }

    @Override
    public List<Reservation> findAll() throws DaoException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Reservation> reservations = new ArrayList<>();
            while (resultSet.next()) {
                reservations.add(buildEntity(resultSet));
            }
            return reservations;
        } catch (SQLException e) {
            throw new DaoException("Couldn't get list of reservations from Database");
        }
    }

    public List<Reservation> findAllByScreeningId(Integer screeningId) throws DaoException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_BY_SCREENING_ID_SQL)) {
            preparedStatement.setInt(1, screeningId);
            var resultSet = preparedStatement.executeQuery();
            List<Reservation> reservations = new ArrayList<>();
            while (resultSet.next()) {
                reservations.add(buildEntity(resultSet));
            }
            return reservations;
        } catch (SQLException e) {
            throw new DaoException("Couldn't get list of reservations by screeningId from Database");
        }
    }

    public List<Reservation> findAllByUserId(Integer userId) throws DaoException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_BY_USER_ID_SQL)) {
            preparedStatement.setInt(1, userId);
            var resultSet = preparedStatement.executeQuery();
            List<Reservation> reservations = new ArrayList<>();
            while (resultSet.next()) {
                reservations.add(buildEntity(resultSet));
            }
            return reservations;
        } catch (SQLException e) {
            throw new DaoException("Couldn't get list of reservations by userId from Database");
        }
    }

    private static Reservation buildEntity(ResultSet resultSet) throws DaoException {
        try {
            return Reservation.builder()
                    .id(resultSet.getObject("id", Integer.class))
                    .userId(resultSet.getObject("user_id", Integer.class))
                    .screeningId(resultSet.getObject("screening_id", Integer.class))
                    .seatId(resultSet.getObject("seat_id", Integer.class))
                    .build();
        } catch (SQLException e) {
            throw new DaoException("Couldn't build reservation");
        }
    }

}
