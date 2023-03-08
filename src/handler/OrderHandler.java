package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.ItemDAO;
import dao.OrderDAO;
import util.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class OrderHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getPath().substring(1).split("/").length > 2) {
            ErrorHandler.handleError(exchange, "Not Found", 400);
            return;
        }

        try (Connection connection = Database.getConnection()) {
            OrderDAO orderDAO = OrderDAO.getInstance(connection, ItemDAO.getInstance(connection));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
