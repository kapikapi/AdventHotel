package com.epam.advent_hotel.db;

import com.epam.advent_hotel.RoomOrder;
import com.epam.advent_hotel.db.connection_pool.Pool;
import org.apache.log4j.Logger;

import java.sql.*;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elizaveta Kapitonova on 13.01.16.
 */
public class DatabaseHandler {
    public static final Logger LOG = Logger.getLogger(DatabaseHandler.class);

    private static final String DB_EXISTS =
            "SELECT COUNT(*) AS ex FROM pg_catalog.pg_database WHERE datname = 'hotel';";
    private static final String CREATE_DB =
            "CREATE DATABASE hotel " +
                    "ENCODING 'UTF8' " +
                    "LC_COLLATE = 'ru_RU.UTF-8' " +
                    "LC_CTYPE = 'ru_RU.UTF-8' " +
                    "TEMPLATE = template0;";

    private static final String CREATE_ACCESS_TYPE = "CREATE TYPE access AS ENUM ('admin', 'user');";
    private static final String CREATE_USERS = "CREATE TABLE IF NOT EXISTS Users (" +
            "user_id SERIAL PRIMARY KEY, " +
            "login VARCHAR (50) UNIQUE NOT NULL," +
            "passhash VARCHAR (255) NOT NULL, " +
            "email VARCHAR (355) UNIQUE NOT NULL, " +
            "access_level access," +
            "name VARCHAR(355)" +
            ");";
    private static final String CREATE_APARTMENT_DESCRIPTION =
            "CREATE TABLE IF NOT EXISTS apartment_description (" +
                    "d_id SERIAL PRIMARY KEY, " +
                    "places INTEGER NOT NULL, " +
                    "class SMALLINT  NOT NULL, " +
                    "cost INTEGER NOT NULL" +
                    ");";
    private static final String CREATE_APARTMENTS =
            "CREATE TABLE IF NOT EXISTS apartments (" +
                    "apt_id SERIAL PRIMARY KEY, " +
                    "number VARCHAR(15) NOT NULL, " +
                    "description INTEGER REFERENCES apartment_description (d_id)" +
                    ");";
    private static final String CREATE_APARTMENTS_OCCUPATION =
            "CREATE TABLE IF NOT EXISTS apartments_occupation (" +
                    "occ_id SERIAL PRIMARY KEY, " +
                    "apartment_id INTEGER REFERENCES apartments (apt_id), " +
                    "user_id INTEGER REFERENCES users (user_id), " +
                    "date_in DATE NOT NULL, " +
                    "date_out DATE NOT NULL);";


    private static final String SET_USER_AS_ADMIN =
            "UPDATE users SET access_level='admin' WHERE login=?";

    private static final String LOGIN_ATTEMPT =
            "SELECT user_id FROM users WHERE login=? AND passhash=?";
    private static final String REGISTER_ATTEMPT =
            "INSERT INTO users (login, passhash, email, access_level, name) VALUES (?, ?, ?, 'user', ?)";
    //in format: date_out, date_in, classOfComfort, places, date_in, date_out
    private static final String SEARCH_ROOMS =
            "SELECT a.apt_id, ao.occ_id, a.number, ad.places, ad.class, " +
                    "ad.cost*? AS cost, ?::DATE AS date_in, ?::DATE AS date_out " +
                    "FROM apartment_description AS ad " +
                    "INNER JOIN apartments AS a ON (a.description=ad.d_id) " +
                    "LEFT JOIN apartments_occupation AS ao " +
                    "ON (ao.apartment_id=a.apt_id) FULL OUTER JOIN " +
                    "(SELECT ao.apartment_id FROM apartments_occupation AS ao " +
                    "WHERE ((?, ?) OVERLAPS (ao.date_in,ao.date_out))) " +
                    "AS v ON (v.apartment_id=ao.apartment_id) " +
                    "WHERE (ad.class=?) AND (ad.places=?) " +
                    "AND ((v.apartment_id IS NULL) OR (ao.apartment_id IS NULL)) " +
                    "ORDER BY ad.places";
    private static final String LOCK_TABLE_OCCUPATION =
            "LOCK TABLE apartments_occupation IN ACCESS EXCLUSIVE MODE;";
    private static final String CURRENT_ROOM =
            "SELECT COUNT(t.occ_id) AS r FROM (SELECT ao.occ_id FROM apartments_occupation AS ao" +
                    " WHERE ao.apartment_id=? AND ((?, ?) OVERLAPS (ao.date_in, ao.date_out))) AS t";
    private static final String EDIT_ORDER_DATES =
            "UPDATE apartments_occupation SET date_in=" +
                    "CASE WHEN (?=1) THEN ? ELSE date_in END, " +
                    "date_out=CASE WHEN (?=1) THEN ? ELSE date_out END " +
                    "WHERE occ_id=?";
    private static final String ROOM_BY_APT_ID =
            "SELECT a.apt_id, a.number, ad.places, ad.class, ad.cost FROM " +
                    "apartment_description AS ad INNER JOIN " +
                    "apartments AS a ON (a.description=ad.d_id)" +
                    "WHERE (a.apt_id=?)";
    private static final String ROOM_BY_ORDER_ID =
            "SELECT a.apt_id, a.number, ad.places, ad.class, ad.cost FROM " +
                    "apartment_description AS ad INNER JOIN " +
                    "apartments AS a ON (a.description=ad.d_id) " +
                    "INNER JOIN apartments_occupation AS ao ON (ao.apartment_id=a.apt_id)" +
                    "WHERE (ao.occ_id=?)";
    private static final String ORDER_BY_ID =
            "SELECT a.apt_id, a.number, ad.places, ad.class, ao.date_in, ao.date_out, " +
                    "ad.cost*(ao.date_out-ao.date_in) AS cost " +
                    "FROM apartments_occupation AS ao INNER JOIN apartments AS a ON (ao.apartment_id=a.apt_id) " +
                    "INNER JOIN apartment_description AS ad ON (a.description=ad.d_id) " +
                    "WHERE (ao.occ_id=?)";
    private static final String SET_ORDER =
            "INSERT INTO apartments_occupation (apartment_id, user_id, date_in, date_out) " +
                    "SELECT ?, ?, ?::DATE, ?::DATE WHERE NOT EXISTS " +
                    "(SELECT * FROM apartments_occupation AS ao WHERE (ao.apartment_id=?) " +
                    "AND ((ao.date_in,ao.date_out) OVERLAPS (?, ?)))";
    private static final String EDIT_ORDER_DELETE =
            "DELETE FROM apartments_occupation WHERE (occ_id=?)";
    private static final String EDIT_ORDER_INSERT =
            "INSERT INTO apartments_occupation (occ_id, apartment_id, user_id, date_in, date_out) " +
                    "SELECT ?, ?, ?, ?::DATE, ?::DATE WHERE NOT EXISTS " +
                    "(SELECT * FROM apartments_occupation AS ao WHERE (ao.apartment_id=?) " +
                    "AND ((ao.date_in,ao.date_out) OVERLAPS (?, ?)))";
    private static final String GET_USER_ID =
            "SELECT user_id FROM users WHERE login=?";
    private static final String GET_USERS_ORDERS =
            "SELECT ao.occ_id, a.apt_id, a.number, ad.places, ad.class, ad.cost*(ao.date_out-ao.date_in) AS cost, ao.date_in, ao.date_out " +
                    "FROM apartment_description AS ad " +
                    "INNER JOIN apartments AS a ON (a.description=ad.d_id) " +
                    "INNER JOIN apartments_occupation AS ao ON (ao.apartment_id=a.apt_id) " +
                    "WHERE (ao.user_id=?) ORDER BY ao.date_in DESC ";
    private static final String DELETE_ORDER =
            "DELETE FROM apartments_occupation WHERE (occ_id=?)";

    private static Connection connection;

    static {
        init();
    }

    public static void init() {
        connection = getConnection();
        try (Statement stmt = connection.createStatement()) {
            ResultSet exists = stmt.executeQuery(DB_EXISTS);
            int hotelDBCount = 0;
            if (exists.first()) {
                hotelDBCount = exists.getInt("ex");
            }
            if (hotelDBCount == 0) {
                stmt.executeUpdate(CREATE_DB);
            }
            connection.prepareStatement(CREATE_ACCESS_TYPE).execute();
            connection.prepareStatement(CREATE_USERS).executeUpdate();
            connection.prepareStatement(CREATE_APARTMENT_DESCRIPTION).executeUpdate();
            connection.prepareStatement(CREATE_APARTMENTS).executeUpdate();
            connection.prepareStatement(CREATE_APARTMENTS_OCCUPATION).executeUpdate();
        } catch (SQLException e) {
            LOG.debug(e.getMessage());
        }
    }

    public static boolean logIn(String login, String password) {
        boolean result = false;
        try (PreparedStatement pstmt = connection.prepareStatement(LOGIN_ATTEMPT,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

            pstmt.setString(1, login);
            pstmt.setString(2, password);
            ResultSet resultSet = pstmt.executeQuery();
            result = resultSet.first();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean register(String name, String login, String password, String email) {
        boolean res = false;
        try (PreparedStatement pstmt = connection.prepareStatement(REGISTER_ATTEMPT)) {
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, name);
            pstmt.executeUpdate();
            res = true;
        } catch (SQLException e) {
            LOG.debug("SQL Failed");
            e.printStackTrace();
        }
        return res;
    }

    public static int getUserId(String login) throws SQLException {
        int res = 0;
        try (PreparedStatement pstmt = connection.prepareStatement(GET_USER_ID,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            pstmt.setString(1, login);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.first()) {
                res = resultSet.getInt("user_id");
            }
        }
        return res;
    }

    public static int removeOrder(int orderId) throws SQLException {
        int res;
        try (PreparedStatement pstmt = connection.prepareStatement(DELETE_ORDER)) {
            pstmt.setInt(1, orderId);
            res = pstmt.executeUpdate();
        }
        LOG.debug(res);
        return res;
    }

    public static int setOrder(int apartment_id, int user_id, LocalDate dateIn, LocalDate dateOut) throws SQLException {
        Date date_in = Date.valueOf(dateIn);
        Date date_out = Date.valueOf(dateOut);
        int res;
        try (PreparedStatement pstmt = connection.prepareStatement(SET_ORDER)) {
            pstmt.setInt(1, apartment_id);
            pstmt.setInt(2, user_id);
            pstmt.setDate(3, date_in);
            pstmt.setDate(4, date_out);
            pstmt.setInt(5, apartment_id);
            pstmt.setDate(6, date_in);
            pstmt.setDate(7, date_out);
            res = pstmt.executeUpdate();
        }
        return res;
    }

    public static int editOrder(int occId, int aptId, int userId, LocalDate dateIn, LocalDate dateOut)
            throws SQLException {
        Date date_in = Date.valueOf(dateIn);
        Date date_out = Date.valueOf(dateOut);
        int resDel;
        int resIns;
        connection.setAutoCommit(false);
        try (PreparedStatement pstmtDel = connection.prepareStatement(EDIT_ORDER_DELETE);
             PreparedStatement pstmt = connection.prepareStatement(EDIT_ORDER_INSERT)) {
            pstmtDel.setInt(1, occId);

            pstmt.setInt(1, occId);
            pstmt.setInt(2, aptId);
            pstmt.setInt(3, userId);
            pstmt.setDate(4, date_in);
            pstmt.setDate(5, date_out);
            pstmt.setInt(6, aptId);
            pstmt.setDate(7, date_in);
            pstmt.setDate(8, date_out);
            resDel = pstmtDel.executeUpdate();
            resIns = pstmt.executeUpdate();
        }
        connection.commit();
        return resDel + resIns;
    }

    private static int isRoomAvailable(int occId, Date date_in, Date date_out) throws SQLException {
        RoomOrder room = getOrderById(occId);
        int overlaped = 0;
        int aptId = room.getId();
        try (PreparedStatement pstmtFree = connection.prepareStatement(CURRENT_ROOM,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            pstmtFree.setInt(1, aptId);
            pstmtFree.setDate(2, date_in);
            pstmtFree.setDate(3, date_out);
            try (ResultSet resultSet = pstmtFree.executeQuery()) {
                if (resultSet.first()) {
                    overlaped = resultSet.getInt("r");
                }
            }
        }
        return overlaped;
    }

    public static boolean isRoomAvailable(int occId, LocalDate dateIn, LocalDate dateOut) throws SQLException {
        Date date_in = Date.valueOf(dateIn);
        Date date_out = Date.valueOf(dateOut);
        boolean res = false;
        int overlaped = isRoomAvailable(occId, date_in, date_out);
        if (overlaped == 1) {
            res = true;
        }
        return res;
    }

    public static int editOrderDates(int occId, LocalDate dateIn, LocalDate dateOut) throws SQLException {
        Date date_in = Date.valueOf(dateIn);
        Date date_out = Date.valueOf(dateOut);
        //RoomOrder room = getOrderById(occId);
        int res;
        connection.setAutoCommit(false);
        try (Statement statement = connection.createStatement();
             PreparedStatement pstmt = connection.prepareStatement(EDIT_ORDER_DATES)) {
            statement.execute(LOCK_TABLE_OCCUPATION);

            int overlaped = isRoomAvailable(occId, date_in, date_out);

            pstmt.setInt(1, overlaped);
            pstmt.setDate(2, date_in);
            pstmt.setInt(3, overlaped);
            pstmt.setDate(4, date_out);
            pstmt.setInt(5, occId);
            res = pstmt.executeUpdate();
        }
        connection.commit();
        return res;
    }

    public static List<RoomOrder> getUsersOrders(int userId) throws SQLException {
        List<RoomOrder> res;
        try (PreparedStatement pstmt = connection.prepareStatement(GET_USERS_ORDERS)) {
            pstmt.setInt(1, userId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                res = resultSetToList(resultSet);
            }
        }
        return res;
    }

    public static RoomOrder getRoomById(int id) throws SQLException {
        RoomOrder roomOrder;
        try (PreparedStatement pstmt = connection.prepareStatement(ROOM_BY_APT_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                roomOrder = resultSetToRoom(resultSet);
            }
        }
        return roomOrder;
    }

    public static RoomOrder getRoomByOrderId(int orderId) throws SQLException {
        RoomOrder roomOrder;
        try (PreparedStatement pstmt = connection.prepareStatement(ROOM_BY_ORDER_ID)) {
            pstmt.setInt(1, orderId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                roomOrder = resultSetToRoom(resultSet);
            }
        }
        return roomOrder;
    }

    public static RoomOrder getOrderById(int orderId) throws SQLException {
        RoomOrder roomOrder;
        try (PreparedStatement pstmt = connection.prepareStatement(ORDER_BY_ID,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            pstmt.setInt(1, orderId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                roomOrder = resultSetToRoom(resultSet);
                if (resultSet.first()) {
                    roomOrder.setDateIn(resultSet.getDate("date_in").toLocalDate());
                    roomOrder.setDateOut(resultSet.getDate("date_out").toLocalDate());
                }
            }
        }
        return roomOrder;
    }

    public static List<RoomOrder> getRoomsByParams(int people, int classOfComfort, LocalDate dateIn, LocalDate dateOut) throws SQLException {
        Date date_in = Date.valueOf(dateIn);
        Date date_out = Date.valueOf(dateOut);
        int duration = daysBetweenDates(dateIn, dateOut);
        List<RoomOrder> res;
        try (PreparedStatement pstmt = connection.prepareStatement(SEARCH_ROOMS)) {
            pstmt.setInt(1, duration);
            pstmt.setDate(2, date_in);
            pstmt.setDate(3, date_out);
            pstmt.setDate(4, date_in);
            pstmt.setDate(5, date_out);
            pstmt.setInt(6, classOfComfort);
            pstmt.setInt(7, people);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                res = resultSetToList(resultSet);
            }
        }
        return res;
    }

    private static RoomOrder resultSetToRoom(ResultSet resultSet) throws SQLException {
        RoomOrder room = new RoomOrder();
        while (resultSet.next()) {
            room.setId(resultSet.getInt("apt_id"));
            room.setPlaces(resultSet.getInt("places"));
            room.setNumber(resultSet.getInt("number"));
            room.setClassOfComfort(resultSet.getInt("class"));
            room.setCost(resultSet.getInt("cost"));
        }
        return room;
    }

    private static List<RoomOrder> resultSetToList(ResultSet resultSet)
            throws SQLException {
        List<RoomOrder> res = new ArrayList<>();
        while (resultSet.next()) {
            RoomOrder room = new RoomOrder();
            room.setOrderId(resultSet.getInt("occ_id"));
            room.setId(resultSet.getInt("apt_id"));
            room.setPlaces(resultSet.getInt("places"));
            room.setNumber(resultSet.getInt("number"));
            room.setClassOfComfort(resultSet.getInt("class"));
            room.setCost(resultSet.getInt("cost"));
            room.setDateIn(resultSet.getDate("date_in").toLocalDate());
            room.setDateOut(resultSet.getDate("date_out").toLocalDate());
            res.add(room);
        }
        return res;
    }

    private static int daysBetweenDates(LocalDate startDate, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        return (int) days;
    }

    private static Connection getConnection() {
        return Pool.getInstance().getConnection();
    }
}
