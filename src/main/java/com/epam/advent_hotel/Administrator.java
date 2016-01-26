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

//    private String classOfComfort;
//    private int people;
//    private Date dateIn;
//    private Date dateOut;


    public List<RoomOrder> getRes(int people, int classOfComfort, LocalDate dateIn, LocalDate dateOut)
            throws SQLException {
        return DatabaseHandler.getRoomsByParams(people, classOfComfort, dateIn, dateOut);
    }

    public RoomOrder getRoomById(int id) throws SQLException{
        return  DatabaseHandler.getRoomById(id);
    }

    public void setOrder(int apartment_id, UserAccount user, LocalDate dateIn, LocalDate dateOut) throws SQLException {
        int userId = user.getUserId();
        DatabaseHandler.setOrder(apartment_id, userId, dateIn, dateOut);
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

//    public int getOrderId(int apt_number, LocalDate dateIn, LocalDate dateOut) throws SQLException {
//        return DatabaseHandler.getOrderId(apt_number, dateIn, dateOut);
//    }
}
