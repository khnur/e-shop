package handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public abstract class Handler {
    static boolean checkPath(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getPath().substring(1).split("/").length > 2) {
            ErrorHandler.handleError(exchange, "Not Found", 404);
            return false;
        }
        return true;
    }
}
