package com.vasche.repository;

import com.vasche.entity.Reservation;
import com.vasche.exception.RepositoryException;
import com.vasche.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservationRepository implements Repository<Integer, Reservation> {
    private static final String DELETE_SQL = """
            DELETE from reservations
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO reservations (user_id, screening_id, seat_id) 
            VALUES (?, ?, ?)
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id, user_id, screening_id, seat_id
            FROM reservations
            """;

    private static final String FIND_ALL_BY_USER_ID_SQL = """
            SELECT r.id, r.user_id, r.screening_id, r.seat_id
            FROM reservations r
            JOIN screenings s on s.id = r.screening_id
            WHERE user_id = ?
            ORDER BY s.start_time
            """;

    private static final String FIND_ALL_BY_SCREENING_ID_SQL = """
            SELECT id, user_id, screening_id, seat_id
            FROM reservations 
            WHERE screening_id = ?
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id, user_id, screening_id, seat_id
            FROM reservations
            WHERE id = ?
            """;

    public ReservationRepository() {
    }

    @Override
    public boolean delete(Integer id) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't delete reservation from Database");
        }
    }

    @Override
    public Reservation save(Reservation reservation) throws RepositoryException {
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
            throw new RepositoryException("Couldn't save reservation in Database");
        }
    }

    @Override
    public void update(Reservation entity) throws RepositoryException {

    }

    @Override
    public Optional<Reservation> findById(Integer id) throws RepositoryException {
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
            throw new RepositoryException("Couldn't get reservation by id from Database");
        }
    }

    @Override
    public List<Reservation> findAll() throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Reservation> reservations = new ArrayList<>();
            while (resultSet.next()) {
                reservations.add(buildEntity(resultSet));
            }
            return reservations;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get list of reservations from Database");
        }
    }

    public List<Reservation> findAllByScreeningId(Integer screeningId) throws RepositoryException {
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
            throw new RepositoryException("Couldn't get list of reservations by screeningId from Database");
        }
    }

    public List<Reservation> findAllByUserId(Integer userId) throws RepositoryException {
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
            throw new RepositoryException("Couldn't get list of reservations by userId from Database");
        }
    }

    private static Reservation buildEntity(ResultSet resultSet) throws RepositoryException {
        try {
            return Reservation.builder()
                    .id(resultSet.getInt("id"))
                    .userId(resultSet.getInt("user_id"))
                    .screeningId(resultSet.getInt("screening_id"))
                    .seatId(resultSet.getInt("seat_id"))
                    .build();
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't build reservation");
        }
    }

}
