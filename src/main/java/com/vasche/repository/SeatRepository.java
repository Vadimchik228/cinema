package com.vasche.repository;

import com.vasche.entity.Seat;
import com.vasche.exception.RepositoryException;
import com.vasche.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SeatRepository implements Repository<Integer, Seat> {

    private static final String DELETE_SQL = """
            DELETE from seats
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO seats (number, line_id) 
            VALUES (?, ?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE seats
            SET number = ?, line_id = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id, number, line_id
            FROM seats
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id, number, line_id
            FROM seats
            WHERE id = ?
            """;

    private static final String FIND_ALL_BY_LINE_ID_SQL = """
            SELECT s.id, s.number, s.line_id
            FROM seats s
            JOIN lines l on l.id = s.line_id
            WHERE l.id = ?
            ORDER BY s.number
            """;

    private static final String FIND_ALL_BY_HALL_ID_SQL = """
            SELECT s.id, s.number, s.line_id
            FROM seats s
            JOIN lines l on l.id = s.line_id
            JOIN halls h on h.id = l.hall_id
            WHERE h.id = ?
            ORDER BY s.number
            """;

    private static final String FIND_ALL_AVAILABLE_BY_SCREENING_ID_AND_HALL_ID = """
            SELECT se.id, se.number, se.line_id
            FROM seats se
            JOIN public.lines l on l.id = se.line_id
            JOIN halls h on h.id = l.hall_id
            WHERE se.id NOT IN (SELECT r.seat_id
                                FROM reservations r
                                JOIN screenings sc on sc.id = r.screening_id
                                WHERE r.screening_id = ? AND sc.hall_id = ?) AND
                  h.id = ?
            ORDER BY se.number
            """;

    private static final String FIND_SEAT_BY_RESERVATION_ID_SQL = """
            SELECT s.id, s.number, s.line_id
            FROM seats s
            JOIN reservations r on s.id = r.seat_id
            WHERE r.id = ?
            """;

    public SeatRepository() {
    }

    @Override
    public boolean delete(Integer id) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't delete seat from Database");
        }
    }

    @Override
    public Seat save(Seat seat) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, seat.getNumber());
            preparedStatement.setInt(2, seat.getLineId());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                seat.setId(generatedKeys.getInt("id"));
            }
            return seat;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Seat seat) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setInt(1, seat.getNumber());
            preparedStatement.setInt(2, seat.getLineId());
            preparedStatement.setInt(3, seat.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't update seat in Database");
        }
    }

    @Override
    public Optional<Seat> findById(Integer id) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            Seat seat = null;
            if (resultSet.next()) {
                seat = buildEntity(resultSet);
            }
            return Optional.ofNullable(seat);
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get seat by id from Database");
        }
    }

    @Override
    public List<Seat> findAll() throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Seat> seats = new ArrayList<>();
            while (resultSet.next()) {
                seats.add(buildEntity(resultSet));
            }
            return seats;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get list of seats from Database");
        }
    }

    public List<Seat> findAllByLineId(Integer lineId) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_BY_LINE_ID_SQL)) {
            preparedStatement.setInt(1, lineId);
            var resultSet = preparedStatement.executeQuery();
            List<Seat> seats = new ArrayList<>();
            while (resultSet.next()) {
                seats.add(buildEntity(resultSet));
            }
            return seats;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get list of seats by lineId from Database");
        }
    }

    public List<Seat> findAllByHallId(Integer hallId) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_BY_HALL_ID_SQL)) {
            preparedStatement.setInt(1, hallId);
            var resultSet = preparedStatement.executeQuery();
            List<Seat> seats = new ArrayList<>();
            while (resultSet.next()) {
                seats.add(buildEntity(resultSet));
            }
            return seats;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get list of seats by hallId from Database");
        }
    }

    public List<Seat> findAllAvailable(Integer screeningId, Integer hallId) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_AVAILABLE_BY_SCREENING_ID_AND_HALL_ID)) {
            preparedStatement.setInt(1, screeningId);
            preparedStatement.setInt(2, hallId);
            preparedStatement.setInt(3, hallId);
            var resultSet = preparedStatement.executeQuery();
            List<Seat> seats = new ArrayList<>();
            while (resultSet.next()) {
                seats.add(buildEntity(resultSet));
            }
            return seats;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get list of seats by screeningId and hallId from Database");
        }
    }

    public Optional<Seat> findByReservationId(Integer reservationId) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_SEAT_BY_RESERVATION_ID_SQL)) {
            preparedStatement.setInt(1, reservationId);
            var resultSet = preparedStatement.executeQuery();
            Seat seat = null;
            if (resultSet.next()) {
                seat = buildEntity(resultSet);
            }
            return Optional.ofNullable(seat);
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get seat by reservationId from Database");
        }
    }

    private static Seat buildEntity(ResultSet resultSet) throws RepositoryException {
        try {
            return Seat.builder()
                    .id(resultSet.getInt("id"))
                    .number(resultSet.getInt("number"))
                    .lineId(resultSet.getInt("line_id"))
                    .build();
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't build seat");
        }
    }
}