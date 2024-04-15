package com.vasche.dao;

import com.vasche.entity.Line;
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
public class LineDao implements Dao<Integer, Line> {

    private static final LineDao INSTANCE = new LineDao();
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
            SELECT *
            FROM lines
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT *
            FROM lines
            WHERE id = ?
            """;

    private static final String FIND_ALL_BY_HALL_ID_SQL = """
            SELECT *
            FROM lines l
            JOIN halls h on h.id = l.hall_id
            WHERE h.id = ?
            ORDER BY l.number
            """;

    private static final String FIND_BY_SEAT_ID_SQL = """
            SELECT *
            FROM lines l
            JOIN seats s on l.id = s.line_id
            WHERE s.id = ?
            """;

    public static LineDao getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Couldn't delete line from Database");
        }
    }

    @Override
    public Line save(Line line) throws DaoException {
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
            throw new DaoException("Couldn't save line in Database");
        }
    }

    @Override
    public void update(Line line) throws DaoException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setInt(1, line.getNumber());
            preparedStatement.setInt(2, line.getHallId());
            preparedStatement.setInt(3, line.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Couldn't update line in Database");
        }
    }

    @Override
    public Optional<Line> findById(Integer id) throws DaoException {
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
            throw new DaoException("Couldn't get line by id from Database");
        }
    }

    @Override
    public List<Line> findAll() throws DaoException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Line> lines = new ArrayList<>();
            while (resultSet.next()) {
                lines.add(buildEntity(resultSet));
            }
            return lines;
        } catch (SQLException e) {
            throw new DaoException("Couldn't get list of lines from Database");
        }
    }

    public Optional<Line> findBySeatId(Integer seatId) throws DaoException {
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
            throw new DaoException("Couldn't get line by seatId from Database");
        }
    }

    public List<Line> findAllByHallId(Integer hallId) throws DaoException {
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
            throw new DaoException("Couldn't get list of lines by hallId from Database");
        }
    }

    private static Line buildEntity(ResultSet resultSet) throws DaoException {
        try {
            return Line.builder()
                    .id(resultSet.getObject("id", Integer.class))
                    .number(resultSet.getObject("number", Integer.class))
                    .hallId(resultSet.getObject("hall_id", Integer.class))
                    .build();
        } catch (SQLException e) {
            throw new DaoException("Couldn't build line");
        }
    }
}
