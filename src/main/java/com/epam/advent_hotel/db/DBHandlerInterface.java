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
 * Interface for working with database.
 *
 * @author Elizaveta Kapitonova
 */
public interface DBHandlerInterface {

    /**
     * Adds row in 'orders' table if input login and email are not already registered.
     *
     * @param name
     * @param login
     * @param password
     * @param email
     * @return
     */
    boolean register(String name, String login, String password, String email);

    /**
     * Checks if input parameters login and password can be found in database
     *
     * @param login
     * @param password
     * @return User object with set fields
     * @throws LoginException when login and password are wrong
     */
    User logIn(String login, String password) throws LoginException;

    /**
     * Gets list of users orders by parts (for paging) by users id
     *
     * @param userId
     * @param offset - depends on currently displayed page
     * @param limit  - depends on number of orders we display on page
     * @return ArrayList of Order objects
     * @throws SQLException
     */
    List<Order> getUsersOrders(int userId, int offset, int limit) throws SQLException;

    /**
     * Gets total number of users orders by his id
     *
     * @param userId
     * @return number of users orders
     * @throws SQLException
     */
    int getUsersNumberOfOrders(int userId) throws SQLException;

    /**
     * Gets values from users row in 'users' table and sets them to User object by his id
     *
     * @param userId
     * @return User object
     * @throws SQLException
     * @throws LoginException if there is no user with such id
     * @see User
     */
    User getUser(int userId) throws SQLException, LoginException;

    /**
     * Sets new order in table 'orders' using input params
     *
     * @param userId
     * @param places         - number of required by user places
     * @param classOfComfort
     * @param dateIn
     * @param dateOut
     * @param comment        first comment to the order. Sets in 'comments' table
     * @return row count
     * @throws SQLException
     */
    int setNewOrder(int userId, int places, int classOfComfort, LocalDate dateIn, LocalDate dateOut, String comment)
            throws SQLException;

    /**
     * Adds row to 'comments' table
     *
     * @param orderId
     * @param comment - comment text
     * @param userId  - id of the user who sent the comment
     * @return row count
     * @throws SQLException
     */
    int setOrderComment(int orderId, String comment, int userId) throws SQLException;

    /**
     * Edits order by its id. First comment in 'comments' is replaced by a new one.
     *
     * @param orderId
     * @param userId
     * @param places
     * @param classOfComfort
     * @param dateIn
     * @param dateOut
     * @param comment
     * @return number of affectsd in 'orders' rows
     * @throws SQLException
     */
    int editOrder(int orderId, int userId, int places, int classOfComfort, LocalDate dateIn, LocalDate dateOut, String comment)
            throws SQLException;

    /**
     * Removes order by its id. Comments to this order are deleted too.
     *
     * @param orderId
     * @return row count
     * @throws SQLException
     */
    int removeOrder(int orderId) throws SQLException;

    /**
     * Gets Order object with set in its fields values from 'orders' by its id
     *
     * @param orderId
     * @return Order object
     * @throws SQLException
     * @see Order
     */
    Order getOrder(int orderId) throws SQLException;

    /**
     * Gets list of all comments to the order by its id. By parts for paging
     *
     * @param orderId
     * @param limit
     * @param offset
     * @return part (from offset to offset + limit)
     * of the ArrayList of Comments object with set fields
     * @throws SQLException
     * @see Comment
     */
    List<Comment> getOrdersComments(int orderId, int limit, int offset) throws SQLException;

    /**
     * Gets number of orders comments
     *
     * @param orderId
     * @return total number of comments to one order
     * @throws SQLException
     */
    int getNumberOfComments(int orderId) throws SQLException;

    /**
     * Gets list of all users. By parts for paging
     * @param limit
     * @param offset
     * @return ArrayList of User objects
     * @throws SQLException
     */
    List<User> getAllUsers(int limit, int offset) throws SQLException, LoginException;

    /**
     * Gets number of all users in 'users' table
     * @return total number of users
     * @throws SQLException
     */
    int getUsersNumber() throws SQLException;

    /**
     * Gets list of all orders. By parts for paging
     *
     * @param limit
     * @param offset
     * @return ArrayList of Order objects from offset to offset + limit
     * @throws SQLException
     * @see Order
     */
    List<Order> getAllOrders(int limit, int offset) throws SQLException;

    /**
     * Gets number of all orders in 'orders' table
     *
     * @return number of all orders
     * @throws SQLException
     */
    int getNumberOfAllOrders() throws SQLException;

    /**
     * Gets list of orders by their status from offset to offset + limit.
     *
     * @param limit
     * @param offset
     * @param status
     * @return ArrayList of Order objects
     * @throws SQLException
     * @see OrderStatus from 'orders'
     */
    List<Order> getOrdersByStatus(int limit, int offset, OrderStatus status) throws SQLException;

    /**
     * Gets number of orders with specified status:
     *
     * @param status
     * @return total number of orders with specified status
     * @throws SQLException
     * @see OrderStatus
     */
    int getNumberOfOrdersByStatus(OrderStatus status) throws SQLException;

    /**
     * Sets ADMIN access level to user
     * @see com.epam.advent_hotel.users.AccessLevel
     * @param userId
     * @return row count
     * @throws SQLException
     */
    int setUserAdmin(int userId) throws SQLException;

    /**
     * Changes status of order in 'orders' table
     *
     * @param orderId
     * @param status
     * @return row count
     * @throws SQLException
     * @see OrderStatus
     */
    int setOrderStatus(int orderId, OrderStatus status) throws SQLException;

    /**
     * Gets parted list of apartments that suit input parameters
     *
     * @param orderId
     * @param limit
     * @param offset
     * @return ArrayList of Apartment objects that match requirements
     * @throws SQLException
     * @see Apartment
     */
    List<Apartment> getSuitableApts(int orderId, int limit, int offset) throws SQLException;

    /**
     * Gets number of suitable to orders parameters apartments
     *
     * @param orderId
     * @return number of apartments that match requirements
     * @throws SQLException
     */
    int getAptNumbers(int orderId) throws SQLException;

    /**
     * Sets apartment's id to order by order's id
     *
     * @param orderId
     * @param aptId
     * @return row count
     * @throws SQLException
     */
    int setOrdersApt(int orderId, int aptId) throws SQLException;


    /**
     * Gets Apartment object by apartment's id
     *
     * @param aptId
     * @return
     * @throws SQLException
     * @see Apartment
     */
    Apartment getApt(int aptId) throws SQLException;

    /**
     * Gets apartment's description by its number and locale
     *
     * @param textNumber - number of description in 'apt_text' table
     * @param locale     - "en" or "ru" Locale
     * @return LocalAptDescription object
     * @throws SQLException
     * @see LocalAptDescription
     */
    LocalAptDescription getDescription(int textNumber, Locale locale) throws SQLException;

    /**
     * Sets cost to order in 'orders' table
     *
     * @param orderId
     * @return row count
     * @throws SQLException
     */
    int setOrderCost(int orderId) throws SQLException;

    /**
     * Sets orders cost to 0
     *
     * @param orderId
     * @return row count
     * @throws SQLException
     */
    int setNullCost(int orderId) throws SQLException;

    /**
     * Sets additional info to order
     *
     * @param orderId
     * @param addInfo
     * @return row count
     * @throws SQLException
     */
    int setAddInfo(int orderId, String addInfo) throws SQLException;
}
