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
    public static final Logger LOG= Logger.getLogger(UserAccount.class);

    private static final String LOGIN_ATTEMPT =
            "SELECT user_id FROM users WHERE login=? AND passhash=?";
    private static final String REGISTER_ATTEMPT =
            "INSERT INTO users (login, passhash, email, access_level, name) VALUES (?, ?, ?, 'user', ?)";
    private static final String SET_USER_AS_ADMIN =
            "UPDATE users SET access_level='admin' WHERE login=?";

    //in format: date_out, date_in, classOfComfort, places, date_in, date_out
    private static final String SEARCH_ROOMS =
            "SELECT DISTINCT a.apt_id, a.number, ad.places, ad.class, ad.cost " +
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


    public  static RoomOrder getRoomById(int id) throws SQLException {
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
        //pstmt.setInt(1, duration);
        pstmt.setInt(1, classOfComfort);
        pstmt.setInt(2, people);
        pstmt.setDate(3, date_in);
        pstmt.setDate(4, date_out);
        ResultSet resultSet = pstmt.executeQuery();

        List<RoomOrder> res = new ArrayList<>();
        while(resultSet.next()) {
            RoomOrder room = new RoomOrder();
            room.setId(resultSet.getInt("apt_id"));
            room.setPlaces(resultSet.getInt("places"));
            room.setNumber(resultSet.getInt("number"));
            if (resultSet.getInt("class")==1) {
                room.setClassOfComfort("Lux");
            } else {
                room.setClassOfComfort("Economy");
            }

            room.setCost(resultSet.getInt("cost")*duration);
            room.setDateIn(dateIn);
            room.setDateOut(dateOut);
            res.add(room);
        }
        return res;
    }

    private static Connection getConnection() {
        return Pool.getInstance().getConnection();
    }
}
