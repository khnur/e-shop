import com.sun.net.httpserver.HttpServer;
import dao.*;
import handler.ItemHandler;
import handler.RegisterHandler;
import handler.UserHandler;
import model.Item;
import model.User;
import security.BasicAuth;
import util.Database;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            table();
            server();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void server() throws IOException {
        String users = User.class.getSimpleName().toLowerCase() + 's';
        String items = Item.class.getSimpleName().toLowerCase() + 's';

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext('/' + users, new UserHandler())
                .setAuthenticator(new BasicAuth(users));
//        httpServer.createContext('/' + items, new ItemHandler())
//                .setAuthenticator(new BasicAuth(items));
        httpServer.createContext("/register", new RegisterHandler());
        httpServer.start();
        System.out.println("Server started on port 8080");
    }

    private static void table() throws SQLException {
        Connection connection = Database.getConnection();
        UserDAO userDAO = UserDAO.getInstance(connection);
//        userDAO.createTable();
//        createAdmin();
//        userDAO.add(new User("heyyy ", "ket", "+777777", "Astana", "qwe42grty"));
//        userDAO.add(new User("hefeyyy ", "kdcet", "+777777", "Astana", "qwe42grty"));
//        userDAO.add(new User("heydcyy ", "ket", "+777sdc777", "Astana", "qwe42grty"));
//        userDAO.add(new User("hescyyy ", "ket", "+777777", "Astana", "qwe42grty"));
//        userDAO.add(new User("hecdsyyy ", "kdet", "+777777", "Astana", "qwe42grty"));
//        userDAO.add(new User("hesdyyy ", "ket", "+777777", "Astana", "qwe42grty"));

        List<User> users = userDAO.getAllUsers();
        for (User user : users)
            System.out.println(user);
    }
    private static void createAdmin() {
        try (Connection connection = Database.getConnection()) {
            UserDAO userDAO = UserDAO.getInstance(connection);
            if (!userDAO.existsByPhone("admin"))
                userDAO.createAdmin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}