package com.epam.advent_hotel.db;

import com.epam.advent_hotel.apartments.Apartment;
import com.epam.advent_hotel.apartments.LocalAptDescription;
import com.epam.advent_hotel.order_room.Comment;
import com.epam.advent_hotel.order_room.Order;
import com.epam.advent_hotel.order_room.OrderStatus;
import com.epam.advent_hotel.users.User;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * Created by Elizaveta Kapitonova on 01.02.16.
 */
public interface DBHandlerInterface {
    // users
    boolean register(String name, String login, String password, String email);
    User logIn(String login, String password) throws LoginException;
    List<Order> getUsersOrders(int userId, int offset, int limit) throws SQLException;
    int getUsersNumberOfOrders(int userId);
    User getUser(int userId) throws SQLException;

    int setNewOrder(int userId, int places, int classOfComfort, LocalDate dateIn, LocalDate dateOut, String comment)
            throws SQLException;
    int setOrderComment(int orderId, String comment, int userId) throws SQLException;
    int editOrder(int orderId, int userId, int places, int classOfComfort, LocalDate dateIn, LocalDate dateOut, String comment)
            throws SQLException;
    int removeOrder(int orderId) throws SQLException;
    Order getOrder(int orderId) throws SQLException;
    List<Comment> getOrdersComments(int orderId, int limit, int offset) throws SQLException;
    int getNumberOfComments(int orderId) throws SQLException;

    List<User> getAllUsers();
    List<Order> getAllOrders(int limit, int offset) throws SQLException;
    int getNumberOfAllOrders() throws SQLException;
    List<Order> getOrdersByStatus(int limit, int offset, OrderStatus status) throws SQLException;
    int getNumberOfOrdersByStatus(OrderStatus status) throws SQLException;
    int setUserAdmin(int userId);
    int setOrderStatus(int orderId, OrderStatus status) throws SQLException;
    List<Apartment> getSuitableApts(int orderId, int limit, int offset) throws SQLException;
    int getAptNumbers(int orderId) throws SQLException;
    int setOrdersApt(int orderId, int aptId) throws SQLException;
    boolean isAptAvailable(int aptId, LocalDate dateIn, LocalDate dateOut);
    Apartment getApt(int aptId) throws SQLException;
    LocalAptDescription getDescription(int textNumber, Locale locale) throws SQLException;
    int setOrderCost(int orderId) throws SQLException;
    int setNullCost(int orderId) throws SQLException;
    int setAddInfo(int orderId, String addInfo) throws SQLException;
}
