package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.ItemDAO;
import util.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class ItemHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!Handler.checkPath(exchange)) return;
        try (Connection connection = Database.getConnection()) {
            ItemDAO itemDAO = ItemDAO.getInstance(connection);
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
