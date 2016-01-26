package com.epam.advent_hotel.db;

import com.epam.advent_hotel.RoomOrder;
import com.epam.advent_hotel.UserAccount;
import com.epam.advent_hotel.db.connection_pool.Pool;
import org.apache.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elizaveta Kapitonova on 13.01.16.
 */
public class DatabaseHandler {
    public static final Logger LOG= Logger.getLogger(DatabaseHandler.class);

    /*
    private static final String GET_USER_LOGIN =
            "SELECT login FROM users WHERE user_id=?";
    private static final String GET_ORDER_ID =
            "SELECT occ_id FROM apartments_occupation AS ao " +
                    "INNER JOIN apartments AS a ON (ao.apartment_id=a.apt_id) " +
                    "WHERE (a.apt_id=?) AND (ao.date_in=?) AND (ao.date_out=?)"; */

    private static final String SET_USER_AS_ADMIN =
            "UPDATE users SET access_level='admin' WHERE login=?";

    private static final String LOGIN_ATTEMPT =
            "SELECT user_id FROM users WHERE login=? AND passhash=?";
    private static final String REGISTER_ATTEMPT =
            "INSERT INTO users (login, passhash, email, access_level, name) VALUES (?, ?, ?, 'user', ?)";
    //in format: date_out, date_in, classOfComfort, places, date_in, date_out
    private static final String SEARCH_ROOMS =
            "SELECT DISTINCT ao.occ_id, a.apt_id, a.number, ad.places, ad.class, " +
                    "ad.cost*? AS cost, ao.date_in, ao.date_out " +
                    "FROM apartment_description AS ad " +
                    "INNER JOIN apartments AS a ON (a.description=ad.d_id) " +
                    "INNER JOIN apartments_occupation AS ao " +
                    "ON (ao.occ_id=ao.occ_id) WHERE (ad.class=?) " +
                    "AND (ad.places>=?) AND NOT ((ao.date_in,ao.date_out) " +
                    "OVERLAPS (?, ?)) ORDER BY ad.places";
    private static final String ROOM_BY_ID =
            "SELECT a.apt_id, a.number, ad.places, ad.class, ad.cost FROM " +
                    "apartment_description AS ad INNER JOIN " +
                    "apartments AS a ON (a.description=ad.d_id)" +
                    "WHERE (a.apt_id=?)";
    private static final String SET_ORDER =
            "INSERT INTO apaertments_occupation (apartment_id, user_id, date_in, date_out)" +
                    "VALUES (?, ?, ?, ?)";
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
    }

    public static boolean logIn(String login, String password) {
        boolean result = false;
        try {
            PreparedStatement pstmt = connection.prepareStatement(LOGIN_ATTEMPT,
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
        try {
            PreparedStatement pstmt = connection.prepareStatement(REGISTER_ATTEMPT);
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

    public static int getUserId(String login) throws SQLException{
        PreparedStatement pstmt = connection.prepareStatement(GET_USER_ID,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        pstmt.setString(1, login);
        ResultSet resultSet = pstmt.executeQuery();
        int res=0;
        if (resultSet.first()) {
            res = resultSet.getInt("user_id");
        }
        return res;
    }

    public static int removeOrder(int orderId) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(DELETE_ORDER);
        pstmt.setInt(1, orderId);
        int res = pstmt.executeUpdate();
        LOG.debug(res);
        return res;
    }

    /*
    public static String getUserLogin(int id) throws SQLException{
        PreparedStatement pstmt = connection.prepareStatement(GET_USER_LOGIN,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        pstmt.setInt(1, id);
        ResultSet resultSet = pstmt.executeQuery();
        String res = "";
        if (resultSet.first()) {
            res = resultSet.getString("login");
        }
        return res;
    }*/

    /*
    public static int getOrderId(int aptId, LocalDate dateIn, LocalDate dateOut) throws SQLException {
        Date date_in = Date.valueOf(dateIn);
        Date date_out = Date.valueOf(dateOut);
        PreparedStatement pstmt = connection.prepareStatement(GET_ORDER_ID,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        pstmt.setInt(1, aptId);
        pstmt.setDate(2, date_in);
        pstmt.setDate(3, date_out);
        ResultSet resultSet = pstmt.executeQuery();
        int res = 0;
        if (resultSet.first()) {
            res = resultSet.getInt("occ_id");
        }
        return res;
    } */

    public static int setOrder(int apartment_id, int user_id, LocalDate dateIn, LocalDate dateOut) throws SQLException
    {
        Date date_in = Date.valueOf(dateIn);
        Date date_out = Date.valueOf(dateOut);
        PreparedStatement pstmt = connection.prepareStatement(SET_ORDER);
        pstmt.setInt(1, apartment_id);
        pstmt.setInt(2, user_id);
        pstmt.setDate(3, date_in);
        pstmt.setDate(4, date_out);
        return pstmt.executeUpdate();
    }

    public static List<RoomOrder> getUsersOrders(int userId) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(GET_USERS_ORDERS);
        pstmt.setInt(1, userId);
        ResultSet resultSet = pstmt.executeQuery();
        return resultSetToList(resultSet);
    }


    public static RoomOrder getRoomById(int id) throws SQLException {
        RoomOrder room = new RoomOrder();
        PreparedStatement pstmt = connection.prepareStatement(ROOM_BY_ID);
        pstmt.setInt(1, id);
        ResultSet resultSet = pstmt.executeQuery();
        while(resultSet.next()) {
            room.setId(resultSet.getInt("apt_id"));
            room.setPlaces(resultSet.getInt("places"));
            room.setNumber(resultSet.getInt("number"));
            if (resultSet.getInt("class") == 1) {
                room.setClassOfComfort("Lux");
            } else {
                room.setClassOfComfort("Economy");
            }
            room.setCost(resultSet.getInt("cost"));
        }
        return room;
    }

    public static List<RoomOrder> getRoomsByParams(int people, int classOfComfort, LocalDate dateIn, LocalDate dateOut) throws SQLException {
        Date date_in = Date.valueOf(dateIn);
        Date date_out = Date.valueOf(dateOut);
        Period period = Period.between(dateIn, dateOut);
        int duration = period.getDays();
        PreparedStatement pstmt = connection.prepareStatement(SEARCH_ROOMS);
        pstmt.setInt(1, duration);
        pstmt.setInt(2, classOfComfort);
        pstmt.setInt(3, people);
        pstmt.setDate(4, date_in);
        pstmt.setDate(5, date_out);
        ResultSet resultSet = pstmt.executeQuery();
        return resultSetToList(resultSet);
    }

    private static List<RoomOrder> resultSetToList(ResultSet resultSet) throws SQLException {
        List<RoomOrder> res = new ArrayList<>();
        while(resultSet.next()) {
            RoomOrder room = new RoomOrder();
            room.setOrderId(resultSet.getInt("occ_id"));
            room.setId(resultSet.getInt("apt_id"));
            room.setPlaces(resultSet.getInt("places"));
            room.setNumber(resultSet.getInt("number"));
            if (resultSet.getInt("class")==1) {
                room.setClassOfComfort("Lux");
            } else {
                room.setClassOfComfort("Economy");
            }

            room.setCost(resultSet.getInt("cost"));

            room.setDateIn(resultSet.getDate("date_in").toLocalDate());
            room.setDateOut(resultSet.getDate("date_out").toLocalDate());
            res.add(room);
        }
        return res;
    }

    private static Connection getConnection() {
        return Pool.getInstance().getConnection();
    }
}
