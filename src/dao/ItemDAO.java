package dao;

import exception.EmptyItemSetException;
import exception.EmptyUserSetException;
import exception.ItemNotExistsException;
import model.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ItemDAO extends DAO {
    private ItemDAO(Connection connection) {
        super(connection, Item.class.getSimpleName().toLowerCase() + 's');
    }

    public static ItemDAO getInstance(Connection connection) {
        return new ItemDAO(connection);
    }

    public boolean createTable() throws SQLException {
        Statement statement = this.connection.createStatement();
        boolean exec = statement.execute("CREATE TABLE " + table + "(" +
                "id SERIAL PRIMARY KEY," +
                Item.getPrice + " SERIAL NOT NULL," +
                Item.getName + " varchar(30) NOT NULL," +
                Item.getManufacturer + " varchar(30) NOT NULL," +
                Item.getDescription + " varchar(30) NOT NULL," +
                Item.getType + " varchar(30) NOT NULL," +
                Item.getCreatedDateTime + " DATE NOT NULL," +
                Item.getModifiedDateTime + " DATE NOT NULL," +
                Item.getIsActive + " BOOLEAN NOT NULL" +
                ");");
        statement.close();
        return exec;
    }

    public void add(Item item) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO " + table +
                        "(" + Item.getPrice +
                        ", " + Item.getName +
                        ", " + Item.getManufacturer +
                        ", " + Item.getDescription +
                        ", " + Item.getType +
                        ", " + Item.getCreatedDateTime +
                        ", " + Item.getModifiedDateTime +
                        ", " + Item.getIsActive +
                        ")" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

        preparedStatement.setInt(1, item.getPrice());
        preparedStatement.setString(2, item.getName());
        preparedStatement.setString(3, item.getManufacturer());
        preparedStatement.setString(4, item.getDescription());
        preparedStatement.setString(5, item.getType());
        preparedStatement.setDate(6, new Date(Calendar.getInstance().getTimeInMillis()));
        preparedStatement.setDate(7, new Date(Calendar.getInstance().getTimeInMillis()));
        preparedStatement.setBoolean(8, true);

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public List<Item> getAllItems() throws SQLException {
        ResultSet resultSet = super.getAll();
        List<Item> items = new ArrayList<>();
        if (!resultSet.next())
            throw new EmptyItemSetException("There is no item");
        do {
            items.add(Item.resultSetToItem(resultSet));
        } while (resultSet.next());
        return items;
    }

    public Item getItemById(long id) throws SQLException {
        ResultSet resultSet = super.getById(id);
        if (!resultSet.next())
            throw new ItemNotExistsException("Item does NOT exits");
        return Item.resultSetToItem(resultSet);
    }
}
