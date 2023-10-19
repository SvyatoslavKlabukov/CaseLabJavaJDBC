package ru.greenatom.DAO;

import ru.greenatom.JdbcConnection;
import ru.greenatom.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgreSqlDao implements Dao<User, Integer>{

    private static final Logger LOGGER =
            Logger.getLogger(PostgreSqlDao.class.getName());
    private final Optional<Connection> connection;

    public PostgreSqlDao() {
        this.connection = JdbcConnection.getConnection();
    }


    @Override
    public Optional<Integer> create(User user) {
        String message = "The user to be added should not be null";
        User nonNullUser = Objects.requireNonNull(user, message);
        String sql = "INSERT INTO "
                + "Users2(FirstName, LastName, Email, Age, Event_date) "
                + "VALUES(?, ?, ?, ?, ?)";

        return connection.flatMap(conn -> {
            Optional<Integer> generatedId = Optional.empty();

            try (PreparedStatement statement =
                         conn.prepareStatement(
                                 sql,
                                 Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, nonNullUser.getFirstName());
                statement.setString(2, nonNullUser.getLastName());
                statement.setString(3, nonNullUser.getEmail());
                statement.setInt(4, nonNullUser.getAge());
                statement.setTimestamp(5, nonNullUser.getEventDate());

                int numberOfInsertedRows = statement.executeUpdate();

                // Retrieve the auto-generated id
                if (numberOfInsertedRows > 0) {
                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            generatedId = Optional.of(resultSet.getInt(1));
                        }
                    }
                }

                LOGGER.log(
                        Level.INFO,
                        "{0} created successfully? {1}",
                        new Object[]{nonNullUser,
                                (numberOfInsertedRows > 0)});
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

            return generatedId;
        });
    }

    @Override
    public void update(User user) {
        String message = "The user to be updated should not be null";
        User nonNullUser = Objects.requireNonNull(user, message);
        String sql = "UPDATE Users2 "
                + "SET "
                + "FirstName = ?, "
                + "LastName = ?, "
                + "Email = ?, "
                + "Age = ?, "
                + "Event_date = ? "
                + "WHERE "
                + "id = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setString(1, nonNullUser.getFirstName());
                statement.setString(2, nonNullUser.getLastName());
                statement.setString(3, nonNullUser.getEmail());
                statement.setInt(4, nonNullUser.getAge());
                statement.setTimestamp(5, nonNullUser.getEventDate());
                statement.setInt(6, nonNullUser.getId());

                int numberOfUpdatedRows = statement.executeUpdate();


                LOGGER.log(Level.INFO, "Was the user updated successfully? {0}",
                        numberOfUpdatedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    @Override
    public void delete(User user) {
        String message = "The user to be deleted should not be null";
        User nonNullUser = Objects.requireNonNull(user, message);
        String sql = "DELETE FROM Users2 WHERE id = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setInt(1, nonNullUser.getId());

                int numberOfDeletedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the user deleted successfully? {0}",
                        numberOfDeletedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    @Override
    public Optional<User> get(int id) {
        return connection.flatMap(conn -> {
            Optional<User> user = Optional.empty();
            String sql = "SELECT * FROM Users2 WHERE id = " + id;

            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {
                    String firstName = resultSet.getString("FirstName");
                    String lastName = resultSet.getString("LastName");
                    String email = resultSet.getString("Email");
                    Integer age = resultSet.getInt("Age");
                    Timestamp eventDate = resultSet.getTimestamp("Event_date");

                    user = Optional.of(
                            new User(id, firstName, lastName, email, age, eventDate));

                    LOGGER.log(Level.INFO, "Found {0} in database", user.get());
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

            return user;
        });
    }

    @Override
    public Collection<User> getAll() {
        Collection<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users2";

        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("FirstName");
                    String lastName = resultSet.getString("LastName");
                    String email = resultSet.getString("Email");
                    Integer age = resultSet.getInt("Age");
                    Timestamp eventDate = resultSet.getTimestamp("Event_date");

                    User user = new User(id, firstName, lastName, email, age, eventDate);

                    users.add(user);

                    LOGGER.log(Level.INFO, "Found {0} in database", user);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return users;
    }

    @Override
    public <F, V> Collection<User> getAll(F field, V value) {
        Collection<User> users = new ArrayList<>();
        String f = field.toString();
        String v = value.toString();
        String sql = "SELECT * FROM Users2 WHERE "+ f +" = "+ v;

        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement()) {
                statement.setFetchSize(10);//пагинация
                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("FirstName");
                    String lastName = resultSet.getString("LastName");
                    String email = resultSet.getString("Email");
                    Integer age = resultSet.getInt("Age");
                    Timestamp eventDate = resultSet.getTimestamp("Event_date");

                    User user = new User(id, firstName, lastName, email, age, eventDate);

                    users.add(user);

                    LOGGER.log(Level.INFO, "Found {0} in database", user);
                }
                resultSet.close();

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return users;
    }
}
