import connection_pool.Pool;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by Elizaveta Kapitonova on 13.01.16.
 */
public class DatabaseHandler {
    public static final Logger LOG= Logger.getLogger(UserAccountClass.class);

    private static final String LOGIN_ATTEMPT =
            "SELECT user_id FROM users WHERE login='%s' AND passhash='%s'";
    private static final String REGISTER_ATTEMPT =
            "INSERT INTO users (login, passhash, email, access_level) VALUES ('%s', '%s', '%s', 'user')";
    private static final String SET_USER_AS_ADMIN =
            "UPDATE users SET access_level='admin' WHERE login=%s";
    private static Connection connection;

    static {
        init();
    }

    public static void init() {
        connection = getConnection();
    }

    public static boolean logIn(String login, String password) {

        try {
            ResultSet resultSet = connection.prepareStatement(String.format(LOGIN_ATTEMPT, login, password),
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery();
            return resultSet.first();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean register(String login, String password, String email) {
        try {
            connection.prepareStatement(String.format(REGISTER_ATTEMPT, login, password, email)).executeUpdate();
            return true;
        } catch (SQLException e) {
            LOG.debug(String.format(REGISTER_ATTEMPT, login, password, email));
            LOG.debug("SQL Failed");
            e.printStackTrace();
            return false;
        }
    }

    // TODO:
    public static List<String> seachRoom(int people,String classOfComfort, Date dateIn, Date dateOut) {
        return null;
    }

    private static Connection getConnection() {
        return Pool.getInstance().getConnection();
    }
}
