package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.ItemDAO;
import exception.EmptyItemSetException;
import exception.ItemNotExistsException;
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
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                handlePostRequest(exchange, itemDAO);
                return;
            }
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                handleGetRequest(exchange, itemDAO);
                return;
            }

            ErrorHandler.handleError(exchange, "Method Not Allowed", 405);
        } catch (EmptyItemSetException | ItemNotExistsException e) {
            ErrorHandler.handleError(exchange, e.getMessage(), 400);
            e.printStackTrace();
        } catch (NumberFormatException | SQLException e) {
            ErrorHandler.handleError(exchange, "Invalid Request Body", 400);
            e.printStackTrace();
        } catch (Exception e) {
            ErrorHandler.handleError(exchange, "Somthing wend wrong", 500);
            e.printStackTrace();
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

        Handler.streamWrite(exchange, response);
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
