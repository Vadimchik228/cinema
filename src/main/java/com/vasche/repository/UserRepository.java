package com.vasche.repository;

import com.vasche.entity.Role;
import com.vasche.entity.User;
import com.vasche.exception.RepositoryException;
import com.vasche.util.ConnectionManager;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements Repository<Integer, User> {
    private static final Logger LOGGER = Logger.getLogger(UserRepository.class);
    private static final String SAVE_SQL = """
            INSERT INTO users (first_name, last_name, email, password, role)
            VALUES (?, ?, ?, ?, ?)
            """;
    public static final String FIND_BY_ID_SQL = """
            SELECT id, first_name, last_name, email, password, role
            FROM users
            WHERE id = ?
            """;
    public static final String FIND_ALL_SQL = """
            SELECT id, first_name, last_name, email, password, role
            FROM users
            """;
    public static final String UPDATE_SQL = """
            UPDATE users
            SET first_name = ?, last_name = ?, email = ?, password = ?, role = ?
            WHERE id = ?
            """;
    private static final String DELETE_SQL = """
            DELETE from users
            WHERE id = ?
            """;
    public static final String FIND_BY_EMAIL_SQL = """
            SELECT id, first_name, last_name, email, password, role
            FROM users
            WHERE email = ?
            """;
    private static final String FIND_BY_RESERVATION_ID_SQL = """
            SELECT u.id, u.first_name, u.last_name, u.email, u.password, u.role
            FROM users u
            JOIN reservations r on u.id = r.user_id
            WHERE r.id = ?
            """;

    public UserRepository() {
    }

    @Override
    public User save(User user) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getRole().name());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt("id"));
            }
            return user;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RepositoryException("Couldn't save user in Database", e);
        }
    }

    @Override
    public Optional<User> findById(Integer id) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = buildEntity(resultSet);
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RepositoryException("Couldn't get user by id from Database", e);
        }
    }

    @Override
    public List<User> findAll() throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(buildEntity(resultSet));
            }
            return users;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RepositoryException("Couldn't get list of users from Database", e);
        }
    }

    @Override
    public void update(User user) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getRole().name());
            preparedStatement.setInt(6, user.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RepositoryException("Couldn't update user in Database", e);
        }
    }

    @Override
    public boolean delete(Integer id) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RepositoryException("Couldn't delete user from Database", e);
        }
    }

    public Optional<User> findByEmail(String email) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_EMAIL_SQL)) {
            preparedStatement.setString(1, email);
            var resultSet = preparedStatement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = buildEntity(resultSet);
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RepositoryException("Couldn't get user by email from Database", e);
        }
    }

    public Optional<User> findByReservationId(Integer reservationId) throws RepositoryException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_RESERVATION_ID_SQL)) {
            preparedStatement.setInt(1, reservationId);

            var resultSet = preparedStatement.executeQuery();
            User usersEntity = null;
            if (resultSet.next()) {
                usersEntity = buildEntity(resultSet);
            }
            return Optional.ofNullable(usersEntity);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RepositoryException("Couldn't get user by reservationId from Database", e);
        }
    }

    private static User buildEntity(ResultSet resultSet) throws RepositoryException {
        try {
            return User.builder()
                    .id(resultSet.getInt("id"))
                    .firstName(resultSet.getObject("first_name", String.class))
                    .lastName(resultSet.getObject("last_name", String.class))
                    .email(resultSet.getObject("email", String.class))
                    .password(resultSet.getObject("password", String.class))
                    .role(Role.valueOf(resultSet.getObject("role", String.class)))
                    .build();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RepositoryException("Couldn't build user", e);
        }
    }
}