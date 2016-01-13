import connection_pool.Pool;
import connection_pool.PoolConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Elizaveta Kapitonova on 13.01.16.
 */
public class DatabaseHandler {

    private static final String LOGIN_ATTEMPT =
            "SELECT user_id FROM Users WHERE login=%s AND passhash=%s";
    private static final String REGISTER_ATTEMPT =
            "INSERT INTO Users (login, passhash, email, access_level) VALUES (%s, %s, %s, 'user')";
    private static final String SET_USER_AS_ADMIN =
            "UPDATE Users SET access_level='admin' WHERE login=%s";
    private static Connection connection;

    static {
        init();
    }

    public static void init() {
        connection = getConnection();
    }

    public static int logIn(String login, String password) {

        try {
            ResultSet resultSet = connection.prepareStatement(String.format(LOGIN_ATTEMPT, login, password)).executeQuery();
            resultSet.first();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int register(String login, String password, String email) {
        try {
            ResultSet resultSet = connection.prepareStatement(String.format(REGISTER_ATTEMPT, login, password, email)).executeQuery();
            resultSet.first();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static Connection getConnection() {
        return Pool.getInstance().getConnection();
    }
}
