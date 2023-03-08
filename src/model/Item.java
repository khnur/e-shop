package model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public final class Item {
    public static final String getPrice = "price_tg";
    public static final String getName = "name";
    public static final String getManufacturer = "manufacturer";
    public static final String getDescription = "description";
    public static final String getType = "type";
    public static final String getCreatedDateTime = "createdDateTime";
    public static final String getModifiedDateTime = "modifiedDateTime";
    public static final String getIsActive = "isActive";
    private long id;
    private int price;
    private String name;
    private String manufacturer;
    private String description;
    private String type;
    private Date createdDateTime;
    private Date modifiedDateTime;
    private boolean isActive;

    public Item() {
    }

    public Item(int price, String name, String manufacturer, String description, String type, Date createdDateTime,
                Date modifiedDateTime, boolean isActive) {
        this.price = price;
        this.name = name;
        this.manufacturer = manufacturer;
        this.description = description;
        this.type = type;
        this.createdDateTime = createdDateTime;
        this.modifiedDateTime = modifiedDateTime;
        this.isActive = isActive;
    }

    public static Item resultSetToItem(ResultSet resultSet) throws SQLException {
        Item item = new Item();
        item.setId(resultSet.getLong("id"));
        item.setPrice(resultSet.getInt(Item.getPrice));
        item.setName(resultSet.getString(Item.getName));
        item.setManufacturer(resultSet.getString(Item.getManufacturer));
        item.setDescription(resultSet.getString(Item.getDescription));
        item.setType(resultSet.getString(Item.getType));
        item.setCreatedDateTime(resultSet.getDate(Item.getCreatedDateTime));
        item.setModifiedDateTime(resultSet.getDate(Item.getModifiedDateTime));
        item.setIsActive(resultSet.getBoolean(Item.getIsActive));
        return item;
    }
    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Date getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(Date modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id && price == item.price && Objects.equals(name, item.name) && Objects.equals(manufacturer, item.manufacturer) && Objects.equals(description, item.description) && Objects.equals(type, item.type) && Objects.equals(createdDateTime, item.createdDateTime) && Objects.equals(modifiedDateTime, item.modifiedDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, name, manufacturer, description, type, createdDateTime, modifiedDateTime);
    }
}
