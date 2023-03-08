package dao;

import exception.UserNotExistsException;

import java.sql.*;

public abstract class DAO {
    protected final String table;
    protected final Connection connection;
    public abstract boolean createTable() throws SQLException;

    protected DAO(Connection connection, String table) {
        this.table = table;
        this.connection = connection;
    }

    protected ResultSet getAll() throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery("SELECT * FROM " + table);
    }

    protected ResultSet getById(long id) throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM " + table + " WHERE id = ?");
        preparedStatement.setLong(1, id);
        return preparedStatement.executeQuery();
    }

    public void updateById(long id, String col, String val) throws SQLException {
        if (!existsById(id))
            throw new UserNotExistsException("There is NO user with such id");

        PreparedStatement preparedStatement = connection
                .prepareStatement("UPDATE " + table + " SET " + col + " = ? WHERE id = ?");
        preparedStatement.setString(1, val);
        preparedStatement.setLong(2, id);

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    protected boolean existsById(long id) throws SQLException {
        PreparedStatement preparedStatement = connection.
                prepareStatement("SELECT 1 FROM " + table + " WHERE id = ?");
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
}
