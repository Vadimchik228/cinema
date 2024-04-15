package com.vasche.dao;

import com.vasche.entity.Hall;
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
public class HallDao implements Dao<Integer, Hall> {

    private static final HallDao INSTANCE = new HallDao();
    private static final String DELETE_SQL = """
            DELETE from halls
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO halls (name) 
            VALUES (?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE halls
            SET name = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT *
            FROM halls
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT *
            FROM halls
            WHERE id = ?
            """;

    private static final String FIND_BY_NAME_SQL = """
            SELECT *
            FROM halls
            WHERE name = ?
            """;

    private static final String FIND_BY_LINE_ID_SQL = """
            SELECT *
            FROM halls h
            JOIN lines l on h.id = l.hall_id
            WHERE l.id = ?
            """;

    public static HallDao getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Couldn't delete hall from Database");
        }
    }

    @Override
    public Hall save(Hall hall) throws DaoException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, hall.getName());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                hall.setId(generatedKeys.getInt("id"));
            }
            return hall;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Hall hall) throws DaoException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, hall.getName());
            preparedStatement.setInt(2, hall.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Couldn't update hall in Database");
        }
    }

    @Override
    public Optional<Hall> findById(Integer id) throws DaoException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            Hall hall = null;
            if (resultSet.next()) {
                hall = buildEntity(resultSet);
            }
            return Optional.ofNullable(hall);
        } catch (SQLException e) {
            throw new DaoException("Couldn't get hall by id from Database");
        }
    }

    @Override
    public List<Hall> findAll() throws DaoException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Hall> halls = new ArrayList<>();
            while (resultSet.next()) {
                halls.add(buildEntity(resultSet));
            }
            return halls;
        } catch (SQLException e) {
            throw new DaoException("Couldn't get list of halls from Database");
        }
    }

    public Optional<Hall> findByName(String name) throws DaoException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_NAME_SQL)) {
            preparedStatement.setString(1, name);
            var resultSet = preparedStatement.executeQuery();
            Hall hall = null;
            if (resultSet.next()) {
                hall = buildEntity(resultSet);
            }
            return Optional.ofNullable(hall);
        } catch (SQLException e) {
            throw new DaoException("Couldn't get hall by name from Database");
        }
    }

    public Optional<Hall> findByLineId(Integer lineId) throws DaoException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_LINE_ID_SQL)) {
            preparedStatement.setInt(1, lineId);
            var resultSet = preparedStatement.executeQuery();
            Hall hall = null;
            if (resultSet.next()) {
                hall = buildEntity(resultSet);
            }
            return Optional.ofNullable(hall);
        } catch (SQLException e) {
            throw new DaoException("Couldn't get hall by lineId from Database");
        }
    }

    private static Hall buildEntity(ResultSet resultSet) throws DaoException {
        try {
            return Hall.builder()
                    .id(resultSet.getObject("id", Integer.class))
                    .name(resultSet.getObject("name", String.class))
                    .build();
        } catch (SQLException e) {
            throw new DaoException("Couldn't build hall");
        }
    }
}

