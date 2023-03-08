package dao;

import model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO extends DAO {
    private OrderItemDAO(Connection connection) {
        super(connection, OrderItem.class.getSimpleName() + 's');
    }

    public static OrderItemDAO getInstance(Connection connection) {
        return new OrderItemDAO(connection);
    }

    public boolean createTable() throws SQLException {
        Statement statement = connection.createStatement();
        boolean exec = statement.execute("CREATE TABLE " + table + "(" +
                "id SERIAL PRIMARY KEY," +
                OrderItem.getOrderId + " SERIAL NOT NULL," +
                OrderItem.getItemId + " SERIAL NOT NULL," +
                OrderItem.getQuantity + " SERIAL NOT NULL," +
                OrderItem.getSubtotal + " SERIAL NOT NULL" +
                ");");
        statement.close();
        return exec;
    }

    public void add(OrderItem orderItem) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO " + table +
                        "(" + OrderItem.getOrderId +
                        ", " + OrderItem.getItemId +
                        ", " + OrderItem.getQuantity +
                        ", " + OrderItem.getSubtotal +
                        ")" +
                        "VALUES(?, ?, ?, ?)"
        );

        preparedStatement.setLong(1, orderItem.getOrderId());
        preparedStatement.setLong(2, orderItem.getItemId());
        preparedStatement.setInt(3, orderItem.getQuantity());
        preparedStatement.setInt(4, orderItem.getSubtotal());

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public List<OrderItem> getAllOrderItems() throws SQLException {
        ResultSet resultSet = super.getAll();
        List<OrderItem> orderItems = new ArrayList<>();
        while (resultSet.next())
            orderItems.add(OrderItem.resultSetToOrderItem(resultSet));
        return orderItems;
    }

    public OrderItem getOrderItemById(long id) throws SQLException {
        ResultSet resultSet = super.getById(id);
        return OrderItem.resultSetToOrderItem(resultSet);
    }
}
