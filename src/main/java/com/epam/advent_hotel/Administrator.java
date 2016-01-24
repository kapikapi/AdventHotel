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
}