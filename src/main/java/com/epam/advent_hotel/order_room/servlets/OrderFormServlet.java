package com.epam.advent_hotel.order_room.servlets;

import com.epam.advent_hotel.db.DBHandler;
import com.epam.advent_hotel.users.User;
import org.apache.log4j.Logger;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by Elizaveta Kapitonova on 01.02.16.
 */
@WebServlet(name = "OrderFormServlet")
public class OrderFormServlet extends HttpServlet {
    public static final Logger LOG= Logger.getLogger(OrderFormServlet.class);
    public static final String ORDER_JSP = "/jsp/order.jsp";
    public static final String USER_PAGE = "/user";

    public static final String FORMATTER_PATTERN = "yyyy-MM-dd";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(ORDER_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String act=request.getParameter("actionName");
        if (act.equals("order")) {
            User user = (User) request.getSession().getAttribute("user");
            int userId = user.getUserId();
            int places = Integer.parseInt(request.getParameter("places"));
            int classOfComfort = Integer.parseInt(request.getParameter("class"));
            String comment = request.getParameter("comment");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTER_PATTERN);
            String date_in = request.getParameter("date_in");
            String date_out = request.getParameter("date_out");
            LocalDate dateIn = LocalDate.parse(date_in, formatter);
            LocalDate dateOut = LocalDate.parse(date_out, formatter);
            if (dateIn.isAfter(LocalDate.now()) && dateIn.isBefore(dateOut)) {
                try {
                    DBHandler.getInstance().setNewOrder(userId, places, classOfComfort, dateIn, dateOut, comment);
                    response.sendRedirect(USER_PAGE);
                } catch (SQLException e) {
                    request.setAttribute("error", true);
                    LOG.error(e.getMessage());
                    fwd(request, response);
                }
            } else {
                request.setAttribute("order_error", true);
                fwd(request, response);
            }
        } else {
            fwd(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        fwd(request, response);
    }
}
