package dao;

import model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO extends DAO {
    private OrderDAO(Connection connection) {
        super(connection, Order.class.getSimpleName().toLowerCase() + 's');
    }

    public static OrderDAO getInstance(Connection connection) {
        return new OrderDAO(connection);
    }

    public boolean createTable() throws SQLException {
        Statement statement = connection.createStatement();
        boolean exec = statement.execute("CREATE TABLE " + table + "(" +
                "id SERIAL PRIMARY KEY," +
                Order.getUserId + " SERIAL NOT NULL," +
                Order.getCreatedDateTime + " varchar(30) NOT NULL," +
                Order.getTotalPrice + " SERIAL NOT NULL" +
                ");");
        statement.close();
        return exec;
    }

    public void add(Order order) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO " + table +
                        "(" + Order.getUserId +
                        ", " + Order.getCreatedDateTime +
                        ", " + Order.getTotalPrice +
                        ")" +
                        "VALUES(?, ?, ?)"
        );

        preparedStatement.setLong(1, order.getUserId());
        preparedStatement.setString(2, order.getCreatedDateTime());
        preparedStatement.setInt(3, order.getTotalPrice());

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public List<Order> getAllOrders() throws SQLException {
        ResultSet resultSet = super.getAll();
        List<Order> orders = new ArrayList<>();
        while (resultSet.next())
            orders.add(Order.resultSetToOrder(resultSet));
        return orders;
    }

    public Order getOrderById(long id) throws SQLException {
        ResultSet resultSet = super.getById(id);
        return Order.resultSetToOrder(resultSet);
    }
}
