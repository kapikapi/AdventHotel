import connection_pool.Pool;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

/**
 * Created by Elizaveta Kapitonova on 13.01.16.
 */
public class DatabaseHandler {
    public static final Logger LOG= Logger.getLogger(UserAccountClass.class);

    private static final String LOGIN_ATTEMPT =
            "SELECT user_id FROM users WHERE login='%s' AND passhash='%s'";
    private static final String REGISTER_ATTEMPT =
            "INSERT INTO users (login, passhash, email, access_level, name) VALUES ('%s', '%s', '%s', 'user', '%s')";
    private static final String SET_USER_AS_ADMIN =
            "UPDATE users SET access_level='admin' WHERE login=%s";
    // in format: date_out, date_in, date_in, date_in, date_out, date_out, class, places
    private static final String SEARCH_ROOMS = "SELECT ad.places, ad.class, ad.cost*('%s'-'%s')" +
            "FROM apartment_description AS ad INNER JOIN apartments AS a ON (a.description=ad.d_id) " +
            "INNER JOIN apartments_occupation AS ao ON (ao.apartment_id=a.apt_id)" +
            "WHERE '%s'>=(SELECT ao.date_out FROM apartments_occupation AS ao WHERE ao.date_out>'%s'" +
            "ORDER BY ao.date_out DESC LIMIT 1) AND '%s'<=(SELECT ao.date_in FROM apartments_occupation " +
            "ORDER BY abs(ao.date_in-'%s') LIMIT 1) AND (ad.class=%s) " +
            "AND (ad.places>=%s) ORDER BY ad.places";

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

    public static boolean register(String name, String login, String password, String email) {
        try {
            connection.prepareStatement(String.format(REGISTER_ATTEMPT, login, password, email, name)).executeUpdate();
            return true;
        } catch (SQLException e) {
            LOG.debug(String.format(REGISTER_ATTEMPT, login, password, email, name));
            LOG.debug("SQL Failed");
            e.printStackTrace();
            return false;
        }
    }

    // TODO:
    public static ResultSet searchRoom(int people,String classOfComfort, LocalDate dateIn, LocalDate dateOut) throws SQLException {
        Date date_in = Date.valueOf(dateIn);
        Date date_out = Date.valueOf(dateOut);
        ResultSet resultSet = connection.prepareStatement(String
                .format(SEARCH_ROOMS, date_out, date_in, date_in, date_in, date_out, date_out, classOfComfort, people))
                .executeQuery();

        return resultSet;
    }

    private static Connection getConnection() {
        return Pool.getInstance().getConnection();
    }
}
