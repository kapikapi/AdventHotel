package com.epam.advent_hotel;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Elizaveta Kapitonova on 26.01.16.
 */
@WebServlet(name = "RemovingServlet")
public class RemovingServlet extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(RemovingServlet.class);
    public static final String REMOVING_JSP = "/jsp/remove_warning.jsp";
    public static final String USER_JSP = "/user";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(REMOVING_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("in removing");
        Administrator administrator = new Administrator();
        String act = request.getParameter("actionName");
        int orderId = Integer.parseInt(request.getParameter("order_id"));
        if (act.equals("removeOrder")) {
            LOG.debug(orderId);
            request.setAttribute("order_id", orderId);
            fwd(request, response);
        } else if (act.equals("confirmed")) {
                try {
                    administrator.removeOrder(orderId);
                    response.sendRedirect(USER_JSP);
                } catch (SQLException e) {
                    LOG.debug(e.getMessage());
                }
            }
        else {
            response.getWriter().write("Error occurred");
            response.getWriter().flush();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        fwd(request, response);
    }
}
