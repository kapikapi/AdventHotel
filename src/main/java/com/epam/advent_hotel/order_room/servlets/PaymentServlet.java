package com.epam.advent_hotel.order_room.servlets;

import com.epam.advent_hotel.apartments.Apartment;
import com.epam.advent_hotel.db.DBHandler;
import com.epam.advent_hotel.order_room.Order;
import com.epam.advent_hotel.order_room.OrderStatus;
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
 * Servlet shows to user final bill.
 * Sets status of order to PAID when user confirms payment.
 *
 * @author Elizaveta Kapitonova
 */
@WebServlet(name = "PaymentServlet")
public class PaymentServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(PaymentServlet.class);
    private static final String PAYMENT_BILL_JSP = "/jsp/user/bill.jsp";
    private static final String ORDER_USER_PAGE = "/user_order";
    private static final String PROPERTY = "local";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(PAYMENT_BILL_JSP).forward(req, resp);
    }

    /**
     * Sets status to PAID.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int orderId = (int) request.getSession().getAttribute("order_id");
        if (null != request.getParameter("actionName")) {
            String act = request.getParameter("actionName");
            if (act.equals("bill")) {
                try {
                    int paidRows = DBHandler.getInstance().setOrderStatus(orderId, OrderStatus.PAID);
                    if (paidRows == 0) {
                        Locale locale = (Locale) request.getSession().getAttribute("locale");
                        ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTY, locale);
                        String logErr = resourceBundle.getString("order.pay.error.log");
                        LOG.error(logErr + " " + String.valueOf(paidRows));
                        request.setAttribute("error", true);
                        fwd(request, response);
                    } else {
                        response.sendRedirect(ORDER_USER_PAGE);
                    }
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
            fwd(request, response);
        }

    }

    /**
     * Shows final bill with all parameters.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
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
    }
}
