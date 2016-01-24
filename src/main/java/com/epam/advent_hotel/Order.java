package com.epam.advent_hotel;

import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Created by Elizaveta Kapitonova on 23.01.16.
 */
public class Order {
    private RoomOrder room;
    private LocalDate dateIn;
    private LocalDate dateOut;
    private UserAccount user;

    public RoomOrder getRoom() {
        return room;
    }

    public void setRoomById(int id) throws SQLException {
        Administrator administrator = new Administrator();
        this.room = administrator.getRoomById(id);
    }

    public LocalDate getDateIn() {
        return dateIn;
    }

    public void setDateIn(LocalDate dateIn) {
        this.dateIn = dateIn;
    }

    public LocalDate getDateOut() {
        return dateOut;
    }

    public void setDateOut(LocalDate dateOut) {
        this.dateOut = dateOut;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }


}
