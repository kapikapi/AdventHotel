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
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Servlet removes order after user confirms removing.
 *
 * @author Elizaveta Kapitonova
 */
@WebServlet(name = "RemoveOrderServlet")
public class RemoveOrderServlet extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(RemoveOrderServlet.class);
    public static final String REMOVING_JSP = "/jsp/remove_warning.jsp";
    public static final String USER_JSP = "/user";
    public static final String ADMIN_JSP = "/admin";
    private static final String PROPERTY = "local";


    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(REMOVING_JSP).forward(req, resp);
    }

    /**
     * Removes order.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //int orderId = (int) request.getSession().getAttribute("order_id");
        int orderId;
        String act = request.getParameter("actionName");
        if (act.equals("confirmed")) {
            orderId = (int) request.getSession().getAttribute("order_id");
            try {
                int removedRows = DBHandler.getInstance().removeOrder(orderId);
                User user = (User) request.getSession().getAttribute("user");
                //boolean correct = rejectOrder(orderId, request);
                if (removedRows == 0) {
                    if (user.getAccessLevel().equals(AccessLevel.ADMIN)) {
                        response.sendRedirect(ADMIN_JSP);
                    } else {
                        response.sendRedirect(USER_JSP);
                    }
                } else {
                    Locale locale = (Locale) request.getSession().getAttribute("locale");
                    ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTY, locale);
                    //String displayErr = resourceBundle.getString(keyName);
                    String logErr = resourceBundle.getString("order.remove.error.log");
                    LOG.error(logErr + " " + String.valueOf(removedRows));
                    request.setAttribute("error", true);
                    fwd(request, response);
                }

            }
            catch (SQLException e) {
                request.setAttribute("error", true);
                LOG.error(e.getMessage());
                fwd(request, response);
            }
        }
        else if (act.equals("remove_order")){
            orderId = Integer.parseInt(request.getParameter("order_id"));
            request.getSession().setAttribute("order_id", orderId);
            fwd(request, response);
        } else {
            fwd(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        fwd(request, response);
    }
}
