package com.epam.advent_hotel.order_room.servlets;

import com.epam.advent_hotel.db.DBHandler;
import com.epam.advent_hotel.order_room.OrderStatus;
import com.epam.advent_hotel.users.AccessLevel;
import com.epam.advent_hotel.users.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Elizaveta Kapitonova on 02.02.16.
 */
@WebServlet(name = "RejectingServlet")
public class RejectingServlet extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(RejectingServlet.class);
    public static final String REJECTING_JSP = "/jsp/reject_warning.jsp";
    public static final String USER_JSP = "/user";
    public static final String ADMIN_JSP = "/admin";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(REJECTING_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String act = request.getParameter("actionName");
        //int orderId = Integer.parseInt(request.getParameter("order_id"));
        int orderId = (int) request.getSession().getAttribute("order_id");
        if (act.equals("reject")) {
//            request.setAttribute("order_id", orderId);
            fwd(request, response);
        } else if (act.equals("confirmed")) {
            try {
                rejectOrder(orderId);
                User user = (User) request.getSession().getAttribute("user");
                if (user.getAccessLevel().equals(AccessLevel.ADMIN)) {
                    response.sendRedirect(ADMIN_JSP);
                } else {
                    response.sendRedirect(USER_JSP);
                }

            } catch (SQLException e) {
                request.setAttribute("error", true);
                LOG.error(e.getMessage());
            }
        }
        else {
            fwd(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        fwd(request, response);
    }

    private void rejectOrder(int orderId) throws SQLException {
        DBHandler.getInstance().setOrderStatus(orderId, OrderStatus.REJECTED);
        DBHandler.getInstance().setOrdersApt(orderId, 0); // drop apt_id
        DBHandler.getInstance().setNullCost(orderId);
    }
}
