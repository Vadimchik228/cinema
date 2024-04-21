package com.vasche.repository;

import com.vasche.entity.Line;
import com.vasche.exception.RepositoryException;
import com.vasche.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LineRepository implements Repository<Integer, Line> {

    private static final String DELETE_SQL = """
            DELETE from lines
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO lines (number, hall_id) 
            VALUES (?, ?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE lines
            SET number = ?, hall_id = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id, number, hall_id
            FROM lines
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id, number, hall_id
            FROM lines
            WHERE id = ?
            """;

    private static final String FIND_ALL_BY_HALL_ID_SQL = """
            SELECT l.id, l.number, l.hall_id
            FROM lines l
            JOIN halls h on h.id = l.hall_id
            WHERE h.id = ?
            ORDER BY l.number
            """;

    private static final String FIND_BY_SEAT_ID_SQL = """
            SELECT l.id, l.number, l.hall_id
            FROM lines l
            JOIN seats s on l.id = s.line_id
            WHERE s.id = ?
            """;

    public LineRepository() {
    }

    @Override
    public boolean delete(Integer id) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't delete line from Database");
        }
    }

    @Override
    public Line save(Line line) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, line.getNumber());
            preparedStatement.setInt(2, line.getHallId());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                line.setId(generatedKeys.getInt("id"));
            }
            return line;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't save line in Database");
        }
    }

    @Override
    public void update(Line line) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setInt(1, line.getNumber());
            preparedStatement.setInt(2, line.getHallId());
            preparedStatement.setInt(3, line.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't update line in Database");
        }
    }

    @Override
    public Optional<Line> findById(Integer id) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            Line line = null;
            if (resultSet.next()) {
                line = buildEntity(resultSet);
            }
            return Optional.ofNullable(line);
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get line by id from Database");
        }
    }

    @Override
    public List<Line> findAll() throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Line> lines = new ArrayList<>();
            while (resultSet.next()) {
                lines.add(buildEntity(resultSet));
            }
            return lines;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get list of lines from Database");
        }
    }

    public Optional<Line> findBySeatId(Integer seatId) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_SEAT_ID_SQL)) {
            preparedStatement.setInt(1, seatId);
            var resultSet = preparedStatement.executeQuery();
            Line line = null;
            if (resultSet.next()) {
                line = buildEntity(resultSet);
            }
            return Optional.ofNullable(line);
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get line by seatId from Database");
        }
    }

    public List<Line> findAllByHallId(Integer hallId) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_BY_HALL_ID_SQL)) {
            preparedStatement.setInt(1, hallId);
            var resultSet = preparedStatement.executeQuery();
            List<Line> lines = new ArrayList<>();
            while (resultSet.next()) {
                lines.add(buildEntity(resultSet));
            }
            return lines;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get list of lines by hallId from Database");
        }
    }

    private static Line buildEntity(ResultSet resultSet) throws RepositoryException {
        try {
            return Line.builder()
                    .id(resultSet.getInt("id"))
                    .number(resultSet.getInt("number"))
                    .hallId(resultSet.getInt("hall_id"))
                    .build();
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't build line");
        }
    }
}
