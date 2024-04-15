package com.vasche.dao;

import com.vasche.entity.Role;
import com.vasche.entity.User;
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
public class UserDao implements Dao<Integer, User> {

    private static final UserDao INSTANCE = new UserDao();
    private static final String DELETE_SQL = """
            DELETE from users
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO users (first_name, last_name, email, password, role) 
            VALUES (?, ?, ?, ?, ?)
            """;

    public static final String UPDATE_SQL = """
            UPDATE users
            SET first_name = ?, last_name = ?, email = ?, password = ?, role = ?
            WHERE id = ?
            """;

    public static final String FIND_ALL_SQL = """
            SELECT *
            FROM users
            """;

    public static final String FIND_BY_ID_SQL = """
            SELECT *
            FROM users
            WHERE id = ?
            """;

    public static final String FIND_BY_EMAIL_SQL = """
            SELECT *
            FROM users
            WHERE email = ?
            """;

    private static final String FIND_ALL_BY_EMAIL_AND_PASSWORD_SQL = """
            SELECT *
            FROM users
            WHERE email = ? AND 
                  password = ?
            """;

    private static final String FIND_ALL_BY_SCREENING_ID_SQL = """
            SELECT *
            FROM users u
            JOIN reservations r on u.id = r.user_id
            JOIN public.screenings s on s.id = r.screening_id
            WHERE s.id = ?
            """;

    private static final String FIND_BY_RESERVATION_ID_SQL = """
            SELECT *
            FROM users u
            JOIN reservations r on u.id = r.user_id
            WHERE r.id = ?
            """;

    public static UserDao getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Couldn't delete user from Database");
        }
    }

    @Override
    public User save(User user) throws DaoException {
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User user) throws DaoException {
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
            throw new DaoException("Couldn't update user in Database");
        }
    }

    @Override
    public Optional<User> findById(Integer id) throws DaoException {
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
            throw new DaoException("Couldn't get user by id from Database");
        }
    }

    @Override
    public List<User> findAll() throws DaoException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(buildEntity(resultSet));
            }
            return users;
        } catch (SQLException e) {
            throw new DaoException("Couldn't get list of users from Database");
        }
    }

    public Optional<User> findByEmail(String email) throws DaoException {
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
            throw new DaoException("Couldn't get user by email from Database");
        }
    }

    public Optional<User> findByEmailAndPassword(String email, String password) throws DaoException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_BY_EMAIL_AND_PASSWORD_SQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            var resultSet = preparedStatement.executeQuery();
            User usersEntity = null;
            if (resultSet.next()) {
                usersEntity = buildEntity(resultSet);
            }
            return Optional.ofNullable(usersEntity);
        } catch (SQLException e) {
            throw new DaoException("Couldn't get user by email and password from Database");
        }
    }

    public Optional<User> findByReservationId(Integer reservationId) throws DaoException {
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
            throw new DaoException("Couldn't get user by reservationId from Database");
        }
    }

    public List<User> findAllByScreeningId(Integer screeningId) throws DaoException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_BY_SCREENING_ID_SQL)) {
            preparedStatement.setInt(1, screeningId);
            var resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(buildEntity(resultSet));
            }
            return users;
        } catch (SQLException e) {
            throw new DaoException("Couldn't get list of users by screeningId from Database");
        }
    }

    private static User buildEntity(ResultSet resultSet) throws DaoException {
        try {
            return User.builder()
                    .id(resultSet.getObject("id", Integer.class))
                    .firstName(resultSet.getObject("first_name", String.class))
                    .lastName(resultSet.getObject("last_name", String.class))
                    .email(resultSet.getObject("email", String.class))
                    .password(resultSet.getObject("password", String.class))
                    .role(Role.valueOf(resultSet.getObject("role", String.class)))
                    .build();
        } catch (SQLException e) {
            throw new DaoException("Couldn't build user");
        }
    }
}