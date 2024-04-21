package com.vasche.repository;

import com.vasche.entity.Hall;
import com.vasche.exception.RepositoryException;
import com.vasche.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HallRepository implements Repository<Integer, Hall> {

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
            SELECT id, name
            FROM halls
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id, name
            FROM halls
            WHERE id = ?
            """;

    private static final String FIND_BY_NAME_SQL = """
            SELECT id, name
            FROM halls
            WHERE name = ?
            """;

    private static final String FIND_BY_LINE_ID_SQL = """
            SELECT h.id, h.name
            FROM halls h
            JOIN lines l on h.id = l.hall_id
            WHERE l.id = ?
            """;

    public HallRepository() {
    }

    @Override
    public boolean delete(Integer id) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't delete hall from Database");
        }
    }

    @Override
    public Hall save(Hall hall) throws RepositoryException {
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
    public void update(Hall hall) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, hall.getName());
            preparedStatement.setInt(2, hall.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't update hall in Database");
        }
    }

    @Override
    public Optional<Hall> findById(Integer id) throws RepositoryException {
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
            throw new RepositoryException("Couldn't get hall by id from Database");
        }
    }

    @Override
    public List<Hall> findAll() throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Hall> halls = new ArrayList<>();
            while (resultSet.next()) {
                halls.add(buildEntity(resultSet));
            }
            return halls;
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't get list of halls from Database");
        }
    }

    public Optional<Hall> findByName(String name) throws RepositoryException {
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
            throw new RepositoryException("Couldn't get hall by name from Database");
        }
    }

    public Optional<Hall> findByLineId(Integer lineId) throws RepositoryException {
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
            throw new RepositoryException("Couldn't get hall by lineId from Database");
        }
    }

    private static Hall buildEntity(ResultSet resultSet) throws RepositoryException {
        try {
            return Hall.builder()
                    .id(resultSet.getInt("id"))
                    .name(resultSet.getObject("name", String.class))
                    .build();
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't build hall");
        }
    }
}

