package dao;

import exception.ItemNotExistsException;
import model.Item;
import model.Order;
import model.OrderItem;
import util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrderDAO extends DAO {
    private final ItemDAO itemDAO;

    private OrderDAO(Connection connection, ItemDAO itemDAO) {
        super(connection, Order.class.getSimpleName().toLowerCase() + 's');
        this.itemDAO = itemDAO;
    }

    public static OrderDAO getInstance(Connection connection, ItemDAO itemDAO) {
        return new OrderDAO(connection, itemDAO);
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

    private void add(Order order) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO " + table +
                        "(" + Order.getUserId +
                        ", " + Order.getTotalPrice +
                        ", " + Order.getCreatedDateTime +
                        ")" +
                        "VALUES(?, ?, ?)"
        );

        preparedStatement.setLong(1, order.getUserId());
        preparedStatement.setInt(2, order.getTotalPrice());
        preparedStatement.setDate(3, order.getCreatedDateTime());

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void createOrder(long userId, List<OrderItem> orderItems) throws SQLException {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            if (!itemDAO.existsById(orderItem.getItemId())) {
                throw new ItemNotExistsException("Item does NOT exist");
            }
            Item item = itemDAO.getItemById(orderItem.getItemId());
            int subtotal = orderItem.getQuantity() * item.getPrice();
            totalPrice += subtotal;
            orderItem.setSubtotal(subtotal);
        }
        this.add(new Order(userId, totalPrice, new Date(Calendar.getInstance().getTimeInMillis())));

        PreparedStatement getLastOrder = connection.prepareStatement("SELECT id FROM " + table,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet lastOrder = getLastOrder.executeQuery();
        lastOrder.next();
        long orderId = lastOrder.getLong("id");

        OrderItemDAO orderItemDAO = OrderItemDAO.getInstance(connection);
        for (OrderItem orderItem : orderItems) {
            orderItemDAO.add(new OrderItem(orderId, orderItem.getItemId(), orderItem.getQuantity(), orderItem.getSubtotal()));
        }
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
