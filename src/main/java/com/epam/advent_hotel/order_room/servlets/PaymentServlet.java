package com.epam.advent_hotel.order_room.servlets;

import com.epam.advent_hotel.apartments.Apartment;
import com.epam.advent_hotel.db.DBHandler;
import com.epam.advent_hotel.order_room.Order;
import com.epam.advent_hotel.order_room.OrderStatus;
import org.apache.log4j.Logger;

import javax.security.auth.login.LoginException;
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
@WebServlet(name = "PaymentServlet")
public class PaymentServlet extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(PaymentServlet.class);
    public static final String PAYMENT_BILL_JSP = "/jsp/bill.jsp";
    public static final String ORDER_USER_PAGE = "/user_order";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(PAYMENT_BILL_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int orderId = (int) request.getSession().getAttribute("order_id");
        if (null != request.getParameter("actionName")) {
            String act = request.getParameter("actionName");
            if (act.equals("bill")) {
                try {
                    DBHandler.getInstance().setOrderStatus(orderId, OrderStatus.PAID);
                    response.sendRedirect(ORDER_USER_PAGE);

                } catch (SQLException e) {
                    e.printStackTrace();
                    request.setAttribute("error", true);
                    LOG.error(e.getMessage());
                    fwd(request, response);
                }
            } else {
                fwd(request, response);
            }
        } else {
            //request.setAttribute("error", "Error occurred. Please, try again.");
            fwd(request, response);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int orderId = (int) request.getSession().getAttribute("order_id");
        try {
            Order order = DBHandler.getInstance().getOrder(orderId);
            Apartment apartment = DBHandler.getInstance().getApt(order.getOrderedAptId());
            request.setAttribute("order", order);
            request.setAttribute("room", apartment);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", true);
            LOG.error(e.getMessage());
        }

        fwd(request, response);
        //doPost(request, response);
    }
}
