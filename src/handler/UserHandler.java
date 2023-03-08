package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.UserDAO;
import exception.EmptyUserSetException;
import exception.UserAlreadyExistsException;
import exception.UserNotExistsException;
import model.User;
import util.Database;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UserHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getPath().substring(1).split("/").length > 2) {
            ErrorHandler.handleError(exchange, "Not Found", 404);
            return;
        }
        try (Connection connection = Database.getConnection()) {
            UserDAO userDAO = UserDAO.getInstance(connection);
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                handlePostRequest(exchange, userDAO);
                return;
            }
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                handleGetRequest(exchange, userDAO);
                return;
            }
            if ("PUT".equalsIgnoreCase(exchange.getRequestMethod())) {
                handlePutRequest(exchange, userDAO);
                return;
            }
            ErrorHandler.handleError(exchange, "Method Not Allowed", 405);
        } catch (EmptyUserSetException | UserNotExistsException | UserAlreadyExistsException e) {
            ErrorHandler.handleError(exchange, e.getMessage(), 400);
            e.printStackTrace();
        } catch (SQLException e) {
            ErrorHandler.handleError(exchange, "Invalid Request Body", 400);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            ErrorHandler.handleError(exchange, "Invalid ID", 400);
            e.printStackTrace();
        } catch (Exception e) {
            ErrorHandler.handleError(exchange, "Something went wrong", 500);
            e.printStackTrace();
        }
    }

    private static void handlePutRequest(HttpExchange httpExchange, UserDAO userDAO) throws IOException, SQLException {
        String path = httpExchange.getRequestURI().getPath().substring(1);
        long id;
        String response;

        if (path.split("/").length > 1) {
            id = Long.parseLong(path.split("/")[1]);
            if (id < 1) {
                ErrorHandler.handleError(httpExchange, "Invalid id", 400);
                return;
            }
        } else {
            ErrorHandler.handleError(httpExchange, "Method Not Allowed", 405);
            return;
        }

        InputStream inputStream = httpExchange.getRequestBody();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();

        String str;
        while ((str = bufferedReader.readLine()) != null)
            stringBuilder.append(str);

        String body = stringBuilder.toString();
        body = body.substring(1, body.length() - 1);
        body = body.replaceAll("\"", "");

        Map<String, String> bodyMap = new HashMap<>();
        Arrays.stream(body.split(","))
                .forEach(string -> bodyMap.put(string.split(":")[0].trim(), string.split(":")[1].trim()));

        if (bodyMap.get(User.getAddress) == null || bodyMap.get(User.getAddress).isBlank()) {
            ErrorHandler.handleError(httpExchange, "Address must be NOT empty", 400);
            return;
        }
        userDAO.updateById(id, User.getAddress, bodyMap.get(User.getAddress));
        response = "{\"message\": \"" + User.class.getSimpleName() + " successfully updated\", \"status\": 200}";
        streamWrite(httpExchange, response);
    }

    static void handlePostRequest(HttpExchange httpExchange, UserDAO userDAO)
            throws IOException, SQLException, NoSuchAlgorithmException {
        String path = httpExchange.getRequestURI().getPath().substring(1);
        if (path.split("/").length > 1) {
            ErrorHandler.handleError(httpExchange, "Not Found", 404);
            return;
        }

        InputStream inputStream = httpExchange.getRequestBody();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();

        String str;
        while ((str = bufferedReader.readLine()) != null)
            stringBuilder.append(str);

        String body = stringBuilder.toString();
        body = body.substring(1, body.length() - 1);
        body = body.replaceAll("\"", "");

        Map<String, String> bodyMap = new HashMap<>();
        Arrays.stream(body.split(","))
                .forEach(string -> bodyMap.put(string.split(":")[0].trim(), string.split(":")[1].trim()));

        userDAO.add(new User(
                bodyMap.get(User.getFirstName),
                bodyMap.get(User.getLastName),
                bodyMap.get(User.getPhone),
                bodyMap.get(User.getAddress),
                bodyMap.get(User.getPassword)
        ));

        String response = "{\"message\": \"" + User.class.getSimpleName() + " successfully created\", \"status\": 200}";

        streamWrite(httpExchange, response);
    }

    private static void handleGetRequest(HttpExchange httpExchange, UserDAO userDAO) throws SQLException, IOException {
        String response;
        String path = httpExchange.getRequestURI().getPath().substring(1);
        if (path.split("/").length > 1) {
            long id = Long.parseLong(path.split("/")[1]);
            if (id > 0) {
                response = userDAO.getUserById(id).toString();
            } else {
                ErrorHandler.handleError(httpExchange, "Invalid Id", 400);
                return;
            }
        } else {
            response = userDAO.getAllUsers().toString();
        }

        streamWrite(httpExchange, response);
    }

    private static void streamWrite(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.getRequestHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
