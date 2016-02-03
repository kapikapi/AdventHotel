package com.epam.advent_hotel.order_room.servlets;

import com.epam.advent_hotel.db.DBHandler;
import com.epam.advent_hotel.order_room.Order;
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
 * Created by Elizaveta Kapitonova on 03.02.16.
 */
@WebServlet(name = "RemoveOrderServlet")
public class RemoveOrderServlet extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(RemoveOrderServlet.class);
    public static final String REMOVING_JSP = "/jsp/remove_warning.jsp";
    public static final String USER_JSP = "/user";
    public static final String ADMIN_JSP = "/admin";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(REMOVING_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int orderId = (int) request.getSession().getAttribute("order_id");
        String act = request.getParameter("actionName");
        if (act.equals("confirmed")) {
            try {
                DBHandler.getInstance().removeOrder(orderId);
                User user = (User) request.getSession().getAttribute("user");
                if (user.getAccessLevel().equals(AccessLevel.ADMIN)) {
                    response.sendRedirect(ADMIN_JSP);
                } else {
                    response.sendRedirect(USER_JSP);
                }

            }
            catch (SQLException e) {
                LOG.debug(e.getMessage());
            }
        }
        // TODO: set error
        else {
            fwd(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        fwd(request, response);
    }
}
