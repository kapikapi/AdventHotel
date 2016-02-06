package com.epam.advent_hotel.order_room.servlets;

import com.epam.advent_hotel.db.DBHandler;
import com.epam.advent_hotel.order_room.Order;
import com.epam.advent_hotel.order_room.OrderStatus;
import com.epam.advent_hotel.users.User;
import org.apache.log4j.Logger;

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
 * Created by Elizaveta Kapitonova on 03.02.16.
 */
@WebServlet(name = "EditingServlet")
public class EditingServlet extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(EditingServlet.class);
    public static final String EDITING_JSP = "/jsp/edit_order.jsp";
    public static final String USER_JSP = "/user";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(EDITING_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        int userId = user.getUserId();

        int orderId = (int) request.getSession().getAttribute("order_id");
        String act = request.getParameter("actionName");
        if (null != request.getParameter("actionName")) {
            if (act.equals("submit_edit")) {
                int places = Integer.parseInt(request.getParameter("places"));
                int classOfComfort = Integer.parseInt(request.getParameter("class"));
                String comment = request.getParameter("comment");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(OrderFormServlet.FORMATTER_PATTERN);
                String date_in = request.getParameter("date_in");
                String date_out = request.getParameter("date_out");
                LocalDate dateIn = LocalDate.parse(date_in, formatter);
                LocalDate dateOut = LocalDate.parse(date_out, formatter);
                if (dateIn.isAfter(LocalDate.now()) && dateIn.isBefore(dateOut)) {
                    try {
                        DBHandler.getInstance().editOrder(orderId, userId, places, classOfComfort, dateIn, dateOut, comment);
                        response.sendRedirect(USER_JSP);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        request.setAttribute("order_error", e.getMessage());
                        LOG.error(e.getMessage());
                        fwd(request, response);
                    }
                } else {
                    request.setAttribute("order_error", true);
                    fwd(request, response);
                }
            } else {
                try {
                    Order order = DBHandler.getInstance().getOrder(orderId);
                    request.setAttribute("order", order);
                } catch (SQLException e) {
                    e.printStackTrace();
                    request.setAttribute("error", true);
                    LOG.error(e.getMessage());
                }

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
