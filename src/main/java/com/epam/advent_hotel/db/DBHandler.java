package com.epam.advent_hotel.db;

import com.epam.advent_hotel.apartments.Apartment;
import com.epam.advent_hotel.apartments.LocalAptDescription;
import com.epam.advent_hotel.db.connection_pool.Pool;
import com.epam.advent_hotel.order_room.Comment;
import com.epam.advent_hotel.order_room.Order;
import com.epam.advent_hotel.order_room.OrderStatus;
import com.epam.advent_hotel.users.AccessLevel;
import com.epam.advent_hotel.users.User;
import org.apache.log4j.Logger;

import javax.security.auth.login.LoginException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Elizaveta Kapitonova on 01.02.16.
 */
public class DBHandler implements DBHandlerInterface {
    private static final Logger LOG = Logger.getLogger(DBHandler.class);

    private static DBHandler INSTANCE;

    public static DBHandler getInstance() {
        if (INSTANCE == null)
            synchronized (Pool.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DBHandler();
                }
            }

        return INSTANCE;
    }

    private static final String CREATE_DB =
            "CREATE DATABASE IF NOT EXISTS advent_hotel " +
                    "CHARACTER SET utf8 COLLATE utf8_unicode_ci;";
    private static final String USE = "USE advent_hotel;";

    private static final String CREATE_USERS =
            "CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "email VARCHAR(255) NOT NULL, " +
                    "login VARCHAR(50) NOT NULL, passhash VARCHAR(355) NOT NULL, " +
                    "access_level ENUM('admin', 'user'), " +
                    "name VARCHAR(355), " +
                    "UNIQUE (login)," +
                    "UNIQUE (email));";
    private static final String CREATE_APT_LOC_TEXT =
            "CREATE TABLE IF NOT EXISTS apt_text (" +
                    "text_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "locale ENUM('en','ru') NOT NULL, " +
                    "apt_loc_text TEXT, " +
                    "text_number INT NOT NULL," +
                    "UNIQUE (text_number, locale))";
    private static final String CREATE_APARTMENTS =
            "CREATE TABLE IF NOT EXISTS apartments (" +
                    "apt_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "number VARCHAR(15) NOT NULL, " +
                    "places INT NOT NULL, " +
                    "class TINYINT NOT NULL, " +
                    "cost INT NOT NULL, " +
                    "description INT, " +
                    "FOREIGN KEY (description) REFERENCES apt_text(text_number) ON DELETE CASCADE ON UPDATE CASCADE, " +
                    "UNIQUE (number));";
    private static final String CREATE_ORDERS =
            "CREATE TABLE IF NOT EXISTS orders (" +
                    "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL," +
                    "places INT NOT NULL, " +
                    "class TINYINT NOT NULL, " +
                    "date_in DATE NOT NULL ," +
                    "date_out DATE NOT NULL ," +
                    "order_apt_id INT," +
                    "order_additional_info TEXT, " +
                    "status ENUM('REQUESTED', 'IN_DISCUSSION', 'APPROVED', 'PAID', 'REJECTED'), " +
                    "cost INT, " +
                    "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE, " +
                    "FOREIGN KEY (order_apt_id) REFERENCES apartments(apt_id) ON DELETE CASCADE ON UPDATE CASCADE);";
    private static final String CREATE_COMMENTS =
            "CREATE TABLE IF NOT EXISTS comments (" +
                    "comment_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "order_id INT NOT NULL, " +
                    "comment TEXT, " +
                    "user_id INT NOT NULL, " +
                    "FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE ON UPDATE CASCADE, " +
                    "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE" +
                    ");";


    private static final String LOGIN_ATTEMPT =
            "SELECT user_id, access_level, users.name, email FROM users WHERE login=? AND passhash=?";
    private static final String REGISTER_ATTEMPT =
            "INSERT INTO users (login, passhash, email, access_level, name) " +
                    "VALUES (?, ?, ?, 'user', ?)";
    private static final String SET_USER_AS_ADMIN =
            "UPDATE users SET access_level='admin' WHERE user_id=?";

    private static final String GET_USERS_ORDERS =
            "SELECT o.id, o.user_id, o.places, o.class, o.date_in, o.date_out, o.order_apt_id, " +
                    "o.order_additional_info, o.status, o.cost AS cost FROM " +
                    "orders AS o WHERE (o.user_id=?) ORDER BY o.date_in DESC LIMIT ? OFFSET ?";
    private static final String USERS_NUMBER_OF_ORDERS =
            "SELECT COUNT(*) AS orders_number " +
                    "FROM  orders AS o WHERE (o.user_id=?)";
    private static final String SET_NEW_ORDER =
            "INSERT INTO orders (user_id, places, class, date_in, date_out, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SET_ORDER_COMMENT =
            "INSERT INTO comments (order_id, comment, user_id) VALUES (?, ?, ?)";
    private static final String GET_ORDERS_BY_STATUS =
            "SELECT id, user_id, places, class, date_in, date_out, order_apt_id, " +
                    "order_additional_info, status, cost FROM orders " +
                    "WHERE (status=?) ORDER BY id DESC " +
                    "LIMIT ? OFFSET ?";
    private static final String NUMBER_OF_ORDERS_BY_STATUS =
            "SELECT COUNT(*) AS orders_number FROM  orders WHERE (status=?) " +
                    "ORDER BY id DESC";
    private static final String GET_ALL_ORDERS =
            "SELECT id, user_id, places, class, date_in, date_out, order_apt_id, " +
                    "order_additional_info, status, cost FROM orders ORDER BY id DESC " +
                    "LIMIT ? OFFSET ?";
    private static final String NUMBER_ALL_ORDERS =
            "SELECT COUNT(*) AS orders_number FROM orders ORDER BY id DESC";
    private static final String SET_ORDER_STATUS =
            "UPDATE orders SET status=? WHERE id=?";
    private static final String GET_ORDER =
            "SELECT id, user_id, places, class, date_in, date_out, order_apt_id, order_additional_info," +
                    "status, cost FROM orders WHERE id=?;";
    private static final String GET_APT_BY_PARAMS =
            "(SELECT a.apt_id, a.number, a.places, a.class, a.cost, a.description FROM apartments AS a " +
                    "LEFT JOIN orders AS o ON (o.order_apt_id=a.apt_id) " +
                    "LEFT JOIN (SELECT order_apt_id FROM orders AS o " +
                    "WHERE (o.date_in BETWEEN ? AND ? OR " +
                    "o.date_out BETWEEN ? AND ?)) AS v " +
                    "ON (v.order_apt_id=o.order_apt_id) " +
                    "WHERE (a.class=?) AND (a.places=?) " +
                    "AND ((v.order_apt_id IS NULL) OR (o.order_apt_id IS NULL))) " +
                    "UNION " +
                    "(SELECT a.apt_id, a.number, a.places, a.class, a.cost, a.description " +
                    "FROM apartments AS a " +
                    "LEFT JOIN orders AS o " +
                    "ON (o.order_apt_id=a.apt_id) " +
                    "RIGHT JOIN " +
                    "(SELECT order_apt_id FROM orders AS o " +
                    "WHERE (o.date_in BETWEEN ? AND ? OR " +
                    "o.date_out BETWEEN ? AND ?)) AS v " +
                    "ON (v.order_apt_id=o.order_apt_id) " +
                    "WHERE (a.class=?) AND (a.places=?) " +
                    "AND ((v.order_apt_id IS NULL) OR (o.order_apt_id IS NULL))) " +
                    "LIMIT ? OFFSET ?;";
    private static final String GET_APTS_NUMBER =
            "SELECT COUNT(*) FROM " +
                    "((SELECT a.apt_id FROM apartments AS a " +
                    "LEFT JOIN orders AS o ON (o.order_apt_id=a.apt_id) " +
                    "LEFT JOIN (SELECT order_apt_id FROM orders AS o " +
                    "WHERE (o.date_in BETWEEN ? AND ? OR " +
                    "o.date_out BETWEEN ? AND ?)) AS v " +
                    "ON (v.order_apt_id=o.order_apt_id) " +
                    "WHERE (a.class=?) AND (a.places=?) " +
                    "AND ((v.order_apt_id IS NULL) OR (o.order_apt_id IS NULL))) " +
                    "UNION " +
                    "(SELECT a.apt_id FROM apartments AS a " +
                    "LEFT JOIN orders AS o " +
                    "ON (o.order_apt_id=a.apt_id) " +
                    "RIGHT JOIN " +
                    "(SELECT order_apt_id FROM orders AS o " +
                    "WHERE (o.date_in BETWEEN ? AND ? OR " +
                    "o.date_out BETWEEN ? AND ?)) AS v " +
                    "ON (v.order_apt_id=o.order_apt_id) " +
                    "WHERE (a.class=?) AND (a.places=?) " +
                    "AND ((v.order_apt_id IS NULL) OR (o.order_apt_id IS NULL)))) AS three";
    private static final String SET_ORDER_APT =
            "UPDATE orders SET order_apt_id=? WHERE id=?";
    private static final String GET_APT_BY_ID =
            "SELECT a.apt_id, a.number, a.places, a.class, a.cost, a.description " +
                    "FROM apartments AS a WHERE a.apt_id=?";
    private static final String GET_DESCRIPTION_BY_NUMBER =
            "SELECT apt_loc_text, text_number, locale FROM apt_text " +
                    "WHERE locale=? AND text_number=?";
    private static final String SET_ORDER_COST =
            "UPDATE orders AS o INNER JOIN apartments AS a ON (a.apt_id=o.order_apt_id) " +
                    "SET o.cost=a.cost*(o.date_out-o.date_in) WHERE o.id=?";
    private static final String SET_NULL_COST =
            "UPDATE orders SET cost=NULL WHERE id=?";
    private static final String REMOVE_ORDER =
            "DELETE FROM orders WHERE id=?";
    private static final String EDIT_ORDER =
            "UPDATE orders SET user_id=?, places=?, class=?, date_in=?, date_out=?, " +
                    "order_apt_id=NULL, status='REQUESTED', cost=NULL " +
                    "WHERE id=?";
    private static final String EDIT_FIRST_ONLY_ORDER_COMMENT =
            "UPDATE comments SET comment=? WHERE order_id=?";
    private static final String GET_USER_BY_ID =
            "SELECT user_id, email, login, access_level, name FROM users " +
                    "WHERE user_id=?";
    private static final String GET_ORDERS_COMMENTS =
            "SELECT comment_id, order_id, comment, user_id FROM comments " +
                    "WHERE order_id=? ORDER BY comment_id DESC LIMIT ? OFFSET ?";
    private static final String GET_COMMENTS_NUMBER =
            "SELECT COUNT(*) AS comments_number FROM comments WHERE order_id=?";


    private static Connection connection;

    {
        init();
    }

    private void init() {
        connection = getConnection();
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CREATE_DB);
            connection.prepareStatement(USE).execute();
            connection.prepareStatement(CREATE_USERS).executeUpdate();
            connection.prepareStatement(CREATE_APT_LOC_TEXT).executeUpdate();
            connection.prepareStatement(CREATE_APARTMENTS).executeUpdate();
            connection.prepareStatement(CREATE_ORDERS).executeUpdate();
            connection.prepareStatement(CREATE_COMMENTS).executeUpdate();
            LOG.debug("Tables created");
        } catch (SQLException e) {
            LOG.debug(e.getMessage());
        }
    }

    @Override
    public User logIn(String login, String password) throws LoginException {
        User user = new User(login);
        try (PreparedStatement pstmt = connection.prepareStatement(LOGIN_ATTEMPT,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            pstmt.setString(1, login);
            pstmt.setString(2, password);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                boolean completed = resultSet.first();
                if (completed) {
                    LOG.debug("completed");
                    user.setUserId(resultSet.getInt("user_id"));
                    user.setEmail(resultSet.getString("email"));
                    user.setAccessLevel(AccessLevel.valueOf(resultSet.getString("access_level").toUpperCase()));
                    user.setName(resultSet.getString("name"));
                    LOG.debug(resultSet.getString("name"));
                } else {
                    throw new LoginException("Wrong login or password");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new LoginException("Wrong login or password");
        }
        return user;
    }

    @Override
    public boolean register(String name, String login, String password, String email) {
        boolean res = false;
        try (PreparedStatement pstmt = connection.prepareStatement(REGISTER_ATTEMPT)) {
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            LOG.debug("Before set: " + name);
            pstmt.setString(4, name);
            pstmt.executeUpdate();
            res = true;
        } catch (SQLException e) {
            LOG.debug("SQL Failed");
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<Order> getUsersOrders(int userId, int offset, int limit) throws SQLException {
        List<Order> res;
        try (PreparedStatement pstmt = connection.prepareStatement(GET_USERS_ORDERS)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            pstmt.setInt(3, offset);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                res = resultSetToOrdersList(resultSet);
            }
        }
        return res;
    }

    @Override
    public int getUsersNumberOfOrders(int userId) {
        int res = 0;
        try (PreparedStatement pstmt = connection.prepareStatement(USERS_NUMBER_OF_ORDERS,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            pstmt.setInt(1, userId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.first()) {
                    res = resultSet.getInt("orders_number");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public User getUser(int userId) throws SQLException {
        User user = null;
        try (PreparedStatement pstmt = connection.prepareStatement(GET_USER_BY_ID)) {
            pstmt.setInt(1, userId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.first()) {
                    LOG.debug("completed");
                    String login = resultSet.getString("login");
                    user = new User(login);
                    user.setUserId(resultSet.getInt("user_id"));
                    user.setEmail(resultSet.getString("email"));
                    user.setAccessLevel(AccessLevel.valueOf(resultSet.getString("access_level").toUpperCase()));
                    user.setName(resultSet.getString("name"));
                }
            }
        } catch (LoginException e) {
            LOG.debug(e.getMessage());
        }
        return user;
    }


    @Override
    public int setNewOrder(int userId, int places, int classOfComfort, LocalDate dateIn, LocalDate dateOut,
                           String comment) throws SQLException {
        Date sqlDateIn = Date.valueOf(dateIn);
        Date sqlDateOut = Date.valueOf(dateOut);
        int newRowId = 0;
        try (PreparedStatement pstmt = connection.prepareStatement(SET_NEW_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, places);
            pstmt.setInt(3, classOfComfort);
            pstmt.setDate(4, sqlDateIn);
            pstmt.setDate(5, sqlDateOut);
            pstmt.setString(6, OrderStatus.REQUESTED.toString());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 1) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newRowId = generatedKeys.getInt(1);
                        if (null != comment) {
                            setOrderComment(newRowId, comment, userId);
                        }
                    }
                }
            } else {
                throw new SQLException("Creating order failed, no ID obtained.");
            }
        }
        return newRowId;
    }

    @Override
    public int setOrderComment(int orderId, String comment, int userId) throws SQLException {
        int res;
        try (PreparedStatement pstmt = connection.prepareStatement(SET_ORDER_COMMENT)) {
            pstmt.setInt(1, orderId);
            pstmt.setString(2, comment);
            pstmt.setInt(3, userId);
            res = pstmt.executeUpdate();
        }
        return res;
    }

    @Override
    public int editOrder(int orderId, int userId, int places, int classOfComfort, LocalDate dateIn, LocalDate dateOut,
                         String comment)
            throws SQLException {
        int res;
        Date sqlDateIn = Date.valueOf(dateIn);
        Date sqlDateOut = Date.valueOf(dateOut);
        try (PreparedStatement pstmt = connection.prepareStatement(EDIT_ORDER)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, places);
            pstmt.setInt(3, classOfComfort);
            pstmt.setDate(4, sqlDateIn);
            pstmt.setDate(5, sqlDateOut);
            pstmt.setInt(6, orderId);
            res = pstmt.executeUpdate();
        }
        try (PreparedStatement pstmt = connection.prepareStatement(EDIT_FIRST_ONLY_ORDER_COMMENT)) {
            pstmt.setString(1, comment);
            pstmt.setInt(2, orderId);
            pstmt.executeUpdate();
        }
        return res;
    }

    @Override
    public int removeOrder(int orderId) throws SQLException {
        int res;
        try (PreparedStatement pstmt = connection.prepareStatement(REMOVE_ORDER)) {
            pstmt.setInt(1, orderId);
            res = pstmt.executeUpdate();
        }
        return res;
    }

    @Override
    public Order getOrder(int orderId) throws SQLException {
        Order order;
        try (PreparedStatement pstmt = connection.prepareStatement(GET_ORDER,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            pstmt.setInt(1, orderId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                order = resultSetToOrder(resultSet);
            }
        }
        return order;
    }

    @Override
    public List<Comment> getOrdersComments(int orderId, int limit, int offset) throws SQLException {
        List<Comment> commentList = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(GET_ORDERS_COMMENTS)) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, limit);
            pstmt.setInt(3, offset);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    if (!(resultSet.getString("comment")).equals("")) {
                    Comment comment = new Comment();
                    comment.setCommentId(resultSet.getInt("comment_id"));
                    comment.setOrderId(resultSet.getInt("order_id"));
                    comment.setText(resultSet.getString("comment"));
                    comment.setUserId(resultSet.getInt("user_id"));
                    commentList.add(comment);
                    }
                }
            }
        }
        return commentList;
    }

    @Override
    public int getNumberOfComments(int orderId) throws SQLException {
        int res = 0;
        try (PreparedStatement pstmt = connection.prepareStatement(GET_COMMENTS_NUMBER,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            pstmt.setInt(1, orderId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.first()) {
                    res = resultSet.getInt("comments_number");
                }
            }
        }
        return res;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public List<Order> getAllOrders(int limit, int offset) throws SQLException {
        List<Order> ordersList;
        try (PreparedStatement pstmt = connection.prepareStatement(GET_ALL_ORDERS)) {
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                ordersList = resultSetToOrdersList(resultSet);
            }
        }
        return ordersList;
    }

    @Override
    public int getNumberOfAllOrders() throws SQLException {
        int res = 0;

        try (PreparedStatement pstmt = connection.prepareStatement(NUMBER_ALL_ORDERS,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.first()) {
                    res = resultSet.getInt("orders_number");
                }
            }
        }
        return res;
    }

    @Override
    public List<Order> getOrdersByStatus(int limit, int offset, OrderStatus status) throws SQLException {
        List<Order> ordersList;
        try (PreparedStatement pstmt = connection.prepareStatement(GET_ORDERS_BY_STATUS)) {
            pstmt.setString(1, status.toString());
            pstmt.setInt(2, limit);
            pstmt.setInt(3, offset);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                ordersList = resultSetToOrdersList(resultSet);
            }
        }
        return ordersList;
    }


    @Override
    public int getNumberOfOrdersByStatus(OrderStatus status) throws SQLException {
        int res = 0;

        try (PreparedStatement pstmt = connection.prepareStatement(NUMBER_OF_ORDERS_BY_STATUS,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            pstmt.setString(1, status.toString());
            try (ResultSet resultSet = pstmt.executeQuery()) {

                if (resultSet.first()) {
                    res = resultSet.getInt("orders_number");
                }
            }
        }
        return res;
    }


    @Override
    public int setUserAdmin(int userId) {
        return 0;
    }

    @Override
    public int setOrderStatus(int orderId, OrderStatus status) throws SQLException {
        int res;
        try (PreparedStatement pstmt = connection.prepareStatement(SET_ORDER_STATUS)) {
            pstmt.setString(1, status.toString());
            pstmt.setInt(2, orderId);
            res = pstmt.executeUpdate();
        }
        return res;
    }

    @Override
    public List<Apartment> getSuitableApts(int orderId, int limit, int offset) throws SQLException {
        List<Apartment> res;
        Order order = getOrder(orderId);
        Date dateIn = Date.valueOf(order.getDateIn());
        Date dateOut = Date.valueOf(order.getDateOut());
        try (PreparedStatement pstmt = connection.prepareStatement(GET_APT_BY_PARAMS)) {
            int j = 0;
            for (int i = 1; i <= 2; i++) {
                pstmt.setDate(1 + j, dateIn);
                pstmt.setDate(2 + j, dateOut);
                pstmt.setDate(3 + j, dateIn);
                pstmt.setDate(4 + j, dateOut);
                pstmt.setInt(5 + j, order.getClassOfComfort());
                pstmt.setInt(6 + j, order.getPlaces());
                j = 6;
            }
            pstmt.setInt(13, limit);
            pstmt.setInt(14, offset);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                res = resultSetToApartmentsList(resultSet);
            }
        }
        return res;
    }

    @Override
    public int getAptNumbers(int orderId) throws SQLException {
        int res = 0;
        Order order = getOrder(orderId);
        Date dateIn = Date.valueOf(order.getDateIn());
        Date dateOut = Date.valueOf(order.getDateOut());
        try (PreparedStatement pstmt = connection.prepareStatement(GET_APTS_NUMBER,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            int j = 0;
            for (int i = 1; i <= 2; i++) {
                pstmt.setDate(1 + j, dateIn);
                pstmt.setDate(2 + j, dateOut);
                pstmt.setDate(3 + j, dateIn);
                pstmt.setDate(4 + j, dateOut);
                pstmt.setInt(5 + j, order.getClassOfComfort());
                pstmt.setInt(6 + j, order.getPlaces());
                j = 6;
            }
            try (ResultSet resultSet = pstmt.executeQuery()) {

                if (resultSet.first()) {
                    res = resultSet.getInt(1);
                }
            }
        }
        return res;
    }

    @Override
    public int setOrdersApt(int orderId, int aptId) throws SQLException {
        int res;
        try (PreparedStatement pstmt = connection.prepareStatement(SET_ORDER_APT)) {
            if (aptId != 0) {
                pstmt.setInt(1, aptId);
            } else {
                pstmt.setNull(1, java.sql.Types.INTEGER);
            }
            pstmt.setInt(2, orderId);
            res = pstmt.executeUpdate();
        }
        return res;
    }

    @Override
    public int setNullCost(int orderId) throws SQLException {
        int res;
        try (PreparedStatement pstmt = connection.prepareStatement(SET_NULL_COST)) {
            pstmt.setInt(1, orderId);
            res = pstmt.executeUpdate();
        }
        return res;
    }

    @Override
    public boolean isAptAvailable(int aptId, LocalDate dateIn, LocalDate dateOut) {
        return false;
    }

    @Override
    public Apartment getApt(int aptId) throws SQLException {
        Apartment apartment;
        try (PreparedStatement pstmt = connection.prepareStatement(GET_APT_BY_ID,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            pstmt.setInt(1, aptId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                apartment = resultSetToApartment(resultSet);
            }
        }
        LOG.debug(apartment.getAptId());
        return apartment;
    }

    @Override
    public LocalAptDescription getDescription(int textNumber, Locale locale) throws SQLException {
        LocalAptDescription localAptDescription = new LocalAptDescription();
        try (PreparedStatement pstmt = connection.prepareStatement(GET_DESCRIPTION_BY_NUMBER,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            pstmt.setString(1, locale.getLanguage());
            pstmt.setInt(2, textNumber);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.first()) {
                    localAptDescription.setTextNumber(resultSet.getInt("text_number"));
                    localAptDescription.setLocale(locale);
                    localAptDescription.setText(resultSet.getString("apt_loc_text"));
                }
            }
        }
        return localAptDescription;
    }

    @Override
    public int setOrderCost(int orderId) throws SQLException {
        int res;
        try (PreparedStatement pstmt = connection.prepareStatement(SET_ORDER_COST)) {
            pstmt.setInt(1, orderId);
            res = pstmt.executeUpdate();
        }
        return res;
    }


    private static Order resultSetToOrder(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        if (resultSet.first()) {
            order.setOrderId(resultSet.getInt("id"));
            order.setUserId(resultSet.getInt("user_id"));
            order.setPlaces(resultSet.getInt("places"));
            order.setClassOfComfort(resultSet.getInt("class"));
            order.setDateIn(resultSet.getDate("date_in").toLocalDate());
            order.setDateOut(resultSet.getDate("date_out").toLocalDate());
            order.setOrderedAptId(resultSet.getInt("order_apt_id"));
            order.setAdditionalInfo(resultSet.getString("order_additional_info"));
            order.setStatus(OrderStatus.valueOf(resultSet.getString("status")));
            order.setCost(resultSet.getInt("cost"));
        }
        return order;
    }

    private static Apartment resultSetToApartment(ResultSet resultSet) throws SQLException {
        Apartment apartment = new Apartment();
        if (resultSet.first()) {
            apartment.setAptId(resultSet.getInt("apt_id"));
            apartment.setNumber(resultSet.getString("number"));
            apartment.setPlaces(resultSet.getInt("places"));
            apartment.setClassOfComfort(resultSet.getInt("class"));
            apartment.setCost(resultSet.getInt("cost"));
            apartment.setDescription(resultSet.getInt("description"));
        }
        return apartment;
    }

    private static List<Order> resultSetToOrdersList(ResultSet resultSet)
            throws SQLException {
        List<Order> res = new ArrayList<>();
        while (resultSet.next()) {
            Order room = new Order();
            room.setOrderId(resultSet.getInt("id"));
            room.setUserId(resultSet.getInt("user_id"));
            room.setPlaces(resultSet.getInt("places"));
            room.setClassOfComfort(resultSet.getInt("class"));
            room.setDateIn(resultSet.getDate("date_in").toLocalDate());
            room.setDateOut(resultSet.getDate("date_out").toLocalDate());
            room.setStatus(OrderStatus.valueOf(resultSet.getString("status")));
            try {
                room.setOrderedAptId(resultSet.getInt("order_apt_id"));
                room.setAdditionalInfo(resultSet.getString("order_additional_info"));
                room.setCost(resultSet.getInt("cost"));
            } catch (NullPointerException e) {
                LOG.debug(e.getMessage());
            }

            res.add(room);
        }
        return res;
    }

    private static List<Apartment> resultSetToApartmentsList(ResultSet resultSet)
            throws SQLException {
        List<Apartment> res = new ArrayList<>();
        while (resultSet.next()) {
            Apartment apartment = new Apartment();
            apartment.setAptId(resultSet.getInt("apt_id"));
            apartment.setNumber(resultSet.getString("number"));
            apartment.setPlaces(resultSet.getInt("places"));
            apartment.setClassOfComfort(resultSet.getInt("class"));
            apartment.setCost(resultSet.getInt("cost"));
            apartment.setDescription(resultSet.getInt("description"));
            res.add(apartment);
        }
        return res;
    }


    private static Connection getConnection() {
        return Pool.getInstance().getConnection();
    }
}

