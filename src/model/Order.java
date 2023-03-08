package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class Order {
    public static final String getUserId = "user_id";
    public static final String getCreatedDateTime = "createdDateTime";
    public static final String getTotalPrice = "totalPrice";
    private long id;
    private long userId;
    private String createdDateTime;
    private int totalPrice;

    public Order() {
    }

    public Order(long userId, String createdDateTime, int totalPrice) {
        this.userId = userId;
        this.createdDateTime = createdDateTime;
        this.totalPrice = totalPrice;
    }

    public static Order resultSetToOrder(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getLong("id"));
        order.setUserId(resultSet.getLong(Order.getUserId));
        order.setCreatedDateTime(resultSet.getString(Order.getCreatedDateTime));
        order.setTotalPrice(resultSet.getInt(Order.getTotalPrice));
        return order;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
