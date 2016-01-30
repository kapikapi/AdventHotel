package com.epam.advent_hotel;

import com.epam.advent_hotel.db.DatabaseHandler;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Elizaveta Kapitonova on 20.01.16.
 */
public class Administrator {
    public static final Logger LOG= Logger.getLogger(Administrator.class);


    public List<RoomOrder> getRes(int people, int classOfComfort, LocalDate dateIn, LocalDate dateOut)
            throws SQLException {
        return DatabaseHandler.getRoomsByParams(people, classOfComfort, dateIn, dateOut);
    }

    public RoomOrder getRoomById(int id) throws SQLException{
        return  DatabaseHandler.getRoomById(id);
    }

    public List<RoomOrder> getUsersOrders(UserAccount user) throws SQLException {
        return DatabaseHandler.getUsersOrders(user.getUserId());
    }


    public RoomOrder getOrderById(int orderId) throws SQLException {
        return DatabaseHandler.getOrderById(orderId);
    }

    public RoomOrder getRoomByOrderId(int orderId) throws SQLException {
        return DatabaseHandler.getRoomByOrderId(orderId);
    }

    public int setOrder(int apartment_id, UserAccount user, LocalDate dateIn, LocalDate dateOut) throws SQLException {
        return DatabaseHandler.setOrder(apartment_id, user.getUserId(), dateIn, dateOut);
    }

    public int editOrder(int orderId, int apartment_id, UserAccount user, LocalDate dateIn, LocalDate dateOut) throws SQLException {
        return DatabaseHandler.editOrder(orderId, apartment_id, user.getUserId(), dateIn, dateOut);
    }

    public boolean removeOrder(int orderId) throws SQLException {
        boolean res = false;
        int affectedRows = DatabaseHandler.removeOrder(orderId);
        if (affectedRows == 1) {
            res=true;
        }
        LOG.debug(res);
        return res;
    }

    public boolean isRoomAvailible(int orderId, LocalDate dateIn, LocalDate dateOut) throws SQLException {
        return DatabaseHandler.isRoomAvailable(orderId, dateIn, dateOut);
    }

    public int editOrderDates(int orderId, LocalDate dateIn, LocalDate dateOut) throws SQLException {
        return DatabaseHandler.editOrderDates(orderId, dateIn, dateOut);
    }

//    public int getOrderId(int apt_number, LocalDate dateIn, LocalDate dateOut) throws SQLException {
//        return DatabaseHandler.getOrderId(apt_number, dateIn, dateOut);
//    }
}
