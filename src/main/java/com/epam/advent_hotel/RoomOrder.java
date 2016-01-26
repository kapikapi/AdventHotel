package com.epam.advent_hotel;

import java.time.LocalDate;

/**
 * Created by Elizaveta Kapitonova on 21.01.16.
 */
public class RoomOrder {

    private int orderId;
    private int id;
    private int number;
    private int places;
    private String classOfComfort;
    private int cost;
    private LocalDate dateIn;
    private LocalDate dateOut;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public String  getClassOfComfort() {
        return classOfComfort;
    }

    public void setClassOfComfort(String classOfComfort) {
        this.classOfComfort = classOfComfort;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

}
