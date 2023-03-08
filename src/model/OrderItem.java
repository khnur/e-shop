package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class OrderItem {
    public static final String getOrderId = "order_id";
    public static final String getItemId = "item_id";
    public static final String getQuantity = "quantity";
    public static final String getSubtotal = "subtotal";
    private long id;
    private long orderId;
    private long itemId;
    private int quantity;
    private int subtotal;

    public OrderItem() {
    }

    public OrderItem(long orderId, long itemId, int quantity, int subtotal) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public static OrderItem resultSetToOrderItem(ResultSet resultSet) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(resultSet.getLong("id"));
        orderItem.setOrderId(resultSet.getLong(OrderItem.getOrderId));
        orderItem.setItemId(resultSet.getLong(OrderItem.getItemId));
        orderItem.setQuantity(resultSet.getInt(OrderItem.getQuantity));
        orderItem.setSubtotal(resultSet.getInt(OrderItem.getSubtotal));
        return orderItem;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "{\"id\": " + id +
                ", \"order_id\": " + "\"" + orderId + "\"" +
                ", \"item_id\": " + "\"" + itemId + "\"" +
                ", \"quantity\": " + "\"" + quantity + "\"" +
                ", \"subtotal\": " + "\"" + subtotal + "\"" + "}";
    }
}
