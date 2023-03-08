package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.ItemDAO;
import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.UserDAO;
import model.Order;
import model.OrderItem;
import model.User;
import util.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getPath().substring(1).split("/").length > 2) {
            ErrorHandler.handleError(exchange, "Not Found", 400);
            return;
        }

        try (Connection connection = Database.getConnection()) {
            OrderDAO orderDAO = OrderDAO.getInstance(connection, ItemDAO.getInstance(connection));
            UserDAO userDAO = UserDAO.getInstance(connection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handlePostRequest(HttpExchange exchange, OrderDAO orderDAO, UserDAO userDAO) throws SQLException, IOException {
        String phone = exchange.getPrincipal().getUsername();
        User user = userDAO.getByPhone(phone);

        String path = exchange.getRequestURI().getPath().substring(1);
        if (path.split("/").length > 1) {
            ErrorHandler.handleError(exchange, "Not Found", 404);
            return;
        }
        Map<String, String> bodyMap = Handler.parseJsonRequest(exchange.getRequestBody());

        List<OrderItem> orderItems = new ArrayList<>();
        for (String key : bodyMap.keySet()) {
            OrderItem orderItem = new OrderItem();
            if (bodyMap.get(OrderItem.getItemId) != null) {

            }
        }

        orderDAO.createOrder(user.getId(), orderItems);

        String response = "{\"message\": \"Order successfully created\", \"status\": 200";
        Handler.streamWrite(exchange, response);
    }
}
