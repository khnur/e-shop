package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.UserDAO;
import util.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class RegisterHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getPath().substring(1).split("/").length > 1) {
            ErrorHandler.handleError(exchange, "Not Found", 404);
            return;
        }
        try (Connection connection = Database.getConnection()) {
            UserDAO userDAO = UserDAO.getInstance(connection);
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                UserHandler.handlePostRequest(exchange, userDAO);
                return;
            }
            ErrorHandler.handleError(exchange, "Method Not Allowed", 405);
        } catch (SQLException e) {
            ErrorHandler.handleError(exchange, "Invalid Request Body", 400);
            e.printStackTrace();
        } catch (Exception e) {
            ErrorHandler.handleError(exchange, "Something went wrong", 500);
            e.printStackTrace();
        }
    }
}
