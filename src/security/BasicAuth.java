package security;

import com.sun.net.httpserver.*;
import exception.UserNotExistsException;
import model.Item;
import model.Order;
import model.User;
import dao.UserDAO;
import util.Database;
import util.HashUtil;
import util.Role;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;

public class BasicAuth extends BasicAuthenticator {
    public BasicAuth(String realm) {
        super(realm);
    }

    public Result authenticate(HttpExchange exchange) {
        Headers headers = exchange.getResponseHeaders();

        String auth = headers.getFirst("Authorization");
        if (auth == null) {
            setAuthHeader(exchange);
            return new Authenticator.Retry(401);
        }

        int sp = auth.indexOf(' ');
        if (sp == -1 || !auth.substring(0, sp).equals("Basic"))
            return new Authenticator.Failure(401);

        byte[] b = Base64.getDecoder().decode(auth.substring(sp + 1));
        String userPass = new String(b, StandardCharsets.UTF_8);
        int colon = userPass.indexOf(':');

        String phone = userPass.substring(0, colon);
        String pass = userPass.substring(colon + 1);

        User user;
        try (Connection connection = Database.getConnection()) {
            UserDAO userDAO = UserDAO.getInstance(connection);
            user = userDAO.getByPhone(phone);
        } catch (UserNotExistsException e) {
            return new Authenticator.Failure(401);
        } catch (SQLException e) {
            return new Authenticator.Failure(500);
        }

        if (checkCredentials(phone, pass)) {
            String pathBase = exchange.getRequestURI().getPath().substring(1).split("/")[0];
            String users = User.class.getSimpleName().toLowerCase() + 's';
            String items = Item.class.getSimpleName().toLowerCase() + 's';
            String orders = Order.class.getSimpleName().toLowerCase() + 's';

            if ("GET".equalsIgnoreCase(exchange.getRequestMethod()))
                return new Authenticator.Success(
                        new HttpPrincipal(phone, realm)
                );
            if (("POST".equalsIgnoreCase(exchange.getRequestMethod()) || "PUT".equalsIgnoreCase(exchange.getRequestMethod()))
                    && (pathBase.equalsIgnoreCase(users) || pathBase.equalsIgnoreCase(items))
                    && user.getRole().equals(Role.ROLE_ADMIN.getRole())) {
                return new Authenticator.Success(
                        new HttpPrincipal(phone, realm)
                );
            }
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod()) && pathBase.equalsIgnoreCase(orders)
                    && user.getRole().equals(Role.ROLE_USER.getRole())) {
                return new Authenticator.Success(new HttpPrincipal(phone, realm));
            } else {
                return new Authenticator.Failure(403);
            }
        } else {
            setAuthHeader(exchange);
            return new Authenticator.Failure(401);
        }
    }

    private void setAuthHeader(HttpExchange exchange) {
        Headers headers = exchange.getResponseHeaders();
        var authString = "Basic realm=" + "\"" + realm + "\"" + ", charset=\"UTF-8\"";
        headers.set("WWW-Authenticate", authString);
    }

    @Override
    public boolean checkCredentials(String phone, String password) {
        try (Connection connection = Database.getConnection()) {
            UserDAO userDAO = UserDAO.getInstance(connection);
            User user = userDAO.getByPhone(phone);
            return HashUtil.isValid(password, user.getPassword());
        } catch (SQLException e) {
            return false;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
