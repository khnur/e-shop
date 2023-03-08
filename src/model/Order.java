package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public final class Order {
    public static final String getUserId = "user_id";
    public static final String getTotalPrice = "total_price";
    public static final String getCreatedDateTime = "created_date";
    private long id;
    private long userId;
    private int totalPrice;
    private Date createdDateTime;

    public Order() {
    }

    public Order(long userId, int totalPrice, Date createdDateTime) {
        this.userId = userId;
        this.createdDateTime = createdDateTime;
        this.totalPrice = totalPrice;
    }

    public static Order resultSetToOrder(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getLong("id"));
        order.setUserId(resultSet.getLong(Order.getUserId));
        order.setTotalPrice(resultSet.getInt(Order.getTotalPrice));
        order.setCreatedDateTime(resultSet.getDate(Order.getCreatedDateTime));
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

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "{\"id\": " + id +
                ", \"user_id\": " + "\"" + userId + "\"" +
                ", \"total_price\": " + "\"" + totalPrice + "\"" +
                ", \"created_date\": " + "\"" + createdDateTime + "\"" + "}";
    }
}
