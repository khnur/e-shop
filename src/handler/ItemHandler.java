package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.ItemDAO;
import model.Item;
import util.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class ItemHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getPath().substring(1).split("/").length > 2) {
            ErrorHandler.handleError(exchange, "Not Found", 404);
            return;
        }
        try (Connection connection = Database.getConnection()) {
            ItemDAO itemDAO = ItemDAO.getInstance(connection);
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void handlePostRequest(HttpExchange exchange, ItemDAO itemDAO) throws IOException, SQLException {
        String path = exchange.getRequestURI().getPath().substring(1);
        if (path.split("/").length > 1) {
            ErrorHandler.handleError(exchange, "Not Found", 404);
            return;
        }
        Map<String, String> bodyMap = Handler.parseJsonRequest(exchange.getRequestBody());
        itemDAO.add(new Item(Integer.parseInt(bodyMap.get(Item.getPrice)), bodyMap.get(Item.getName), bodyMap.get(Item.getManufacturer),
                bodyMap.get(Item.getDescription), bodyMap.get(Item.getType)));
        String response = "{\"message\": \"Item successfully created\", \"status\": 200}";

        Handler.streamWrite(exchange,response);
    }
    private void handleGetRequest(HttpExchange exchange, ItemDAO itemDAO) throws SQLException, IOException {
        String response;
        String path = exchange.getRequestURI().getPath().substring(1);
        if (path.split("/").length > 1) {
            long id = Long.parseLong(path.split("/")[1]);
            if (id > 0) {
                response = itemDAO.getItemById(id).toString();
            } else {
                ErrorHandler.handleError(exchange, "Invalid id", 400);
                return;
            }
        } else {
            response = itemDAO.getAllItems().toString();
        }
        Handler.streamWrite(exchange, response);
    }
}
