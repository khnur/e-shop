package dao;

import exception.EmptyUserSetException;
import exception.UserNotExistsException;
import model.User;
import util.HashUtil;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends DAO {
    private UserDAO(Connection connection) {
        super(connection, User.class.getSimpleName().toLowerCase() + 's');
    }

    public static UserDAO getInstance(Connection connection) {
        return new UserDAO(connection);
    }

    public boolean createTable() throws SQLException {
        Statement statement = connection.createStatement();
        boolean exec = statement.execute("CREATE TABLE " + table + "(" +
                "id SERIAL PRIMARY KEY," +
                User.getFirstName + " varchar(30) NOT NULL," +
                User.getLastName + " varchar(30) NOT NULL," +
                User.getPhone + " varchar(30) NOT NULL," +
                User.getAddress + " varchar(50) NOT NULL," +
                User.getPassword + " varchar(150) NOT NULL," +
                User.getRole + " varchar(30) NOT NULL" +
                ");");
        statement.close();
        return exec;
    }

    public void add(User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO " + table +
                        "(" + User.getFirstName +
                        ", " + User.getLastName +
                        ", " + User.getPhone +
                        ", " + User.getAddress +
                        ", " + User.getPassword +
                        ", " + User.getRole +
                        ")" +
                        "VALUES (?, ?, ?, ?, ?, ?)");
        preparedStatement.setString(1, user.getFirstname());
        preparedStatement.setString(2, user.getLastname());
        preparedStatement.setString(3, user.getPhone());
        preparedStatement.setString(4, user.getAddress());
        preparedStatement.setBytes(5, user.getPassword());
        preparedStatement.setString(6, user.getRole());

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    public void createAdmin() throws NoSuchAlgorithmException, SQLException {
        byte[] hashPassword = HashUtil.hashPassword("admin");
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO " + table +
                        "(" + User.getFirstName +
                        ", " + User.getLastName +
                        ", " + User.getPhone +
                        ", " + User.getAddress +
                        ", " + User.getPassword +
                        ", " + User.getRole +
                        ")" +
                        "VALUES (?, ?, ?, ?, ?, ?)");
        preparedStatement.setString(1, "admin");
        preparedStatement.setString(2, "admin");
        preparedStatement.setString(3, "admin");
        preparedStatement.setString(4, "admin");
        preparedStatement.setBytes(5, hashPassword);
        preparedStatement.setString(6, "admin");

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public List<User> getAllUsers() throws SQLException {
        ResultSet resultSet = super.getAll();
        List<User> users = new ArrayList<>();
        if (!resultSet.next())
            throw new EmptyUserSetException("There is no user");

        do {
            users.add(User.resultSetToUser(resultSet));
        } while (resultSet.next());

        return users;
    }

    public User getUserById(long id) throws SQLException {
        ResultSet resultSet = super.getById(id);
        if (!resultSet.next())
            throw new UserNotExistsException("User does NOT exist");
        return User.resultSetToUser(resultSet);
    }

    public User getByPhone(String phone) throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM " + table + " WHERE phone = ?");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next())
            throw new UserNotExistsException("User does NOT exist");
        return User.resultSetToUser(resultSet);
    }

    public boolean existsByPhone(String phone) throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT 1 FROM " + table + " WHERE phone = ?");
        preparedStatement.setString(1, phone);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
}
