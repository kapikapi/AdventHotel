package com.epam.advent_hotel.admin;

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
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Elizaveta Kapitonova on 01.02.16.
 */
@WebServlet(name = "AdminServlet")
public class AdminServlet extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(AdminServlet.class);
    public static final String ADMIN_JSP = "/jsp/admin.jsp";

    private static final int PER_PAGE = 10;
    private static final String PROPERTY = "local";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(ADMIN_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("in doPost");
        List<Order> ordersList;
        int page = 1;
        if (null != request.getParameter("page")) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        int offset = (page - 1) * PER_PAGE;
        int numberOfOrders;
        OrderStatus status = null;
        Locale locale = (Locale) request.getSession().getAttribute("locale");
        ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTY, locale);
        String heading = resourceBundle.getString("admin.orders.heading.all");
        if (null != request.getParameter("actionName")) {
            String act = request.getParameter("actionName");
            LOG.debug(act);
            switch (act) {
                case "requested":
                    status = OrderStatus.REQUESTED;
                    heading = resourceBundle.getString("admin.orders.heading.requested");
                    break;
                case "in_discussion":
                    status = OrderStatus.IN_DISCUSSION;
                    heading = resourceBundle.getString("admin.orders.heading.in_discussion");
                    break;
                case "approved":
                    status = OrderStatus.APPROVED;
                    heading = resourceBundle.getString("admin.orders.heading.approved");
                    break;
                case "paid":
                    status = OrderStatus.PAID;
                    heading = resourceBundle.getString("admin.orders.heading.paid");
                    break;
                case "rejected":
                    status = OrderStatus.REJECTED;
                    heading = resourceBundle.getString("admin.orders.heading.rejected");
                    break;
            }
        }
        try {
            if (null == status) {
                ordersList = DBHandler.getInstance().getAllOrders(PER_PAGE, offset);
                numberOfOrders = DBHandler.getInstance().getNumberOfAllOrders();
            } else {
                ordersList = DBHandler.getInstance().getOrdersByStatus(PER_PAGE, offset, status);
                numberOfOrders = DBHandler.getInstance().getNumberOfOrdersByStatus(status);
            }

            LOG.debug(numberOfOrders);
            int noOfPages = (int) Math.ceil(numberOfOrders * 1.0 / PER_PAGE);
            request.setAttribute("noOfPages", noOfPages);
            request.setAttribute("currentPage", page);
            request.setAttribute("heading", heading);
            request.setAttribute("orders_list", ordersList);
            if (ordersList.isEmpty()) {
                request.setAttribute("no_result", true);
            }
        } catch (SQLException e) {
            request.setAttribute("no_result", true);
            request.setAttribute("error", true);
            LOG.debug(e.getMessage());
            e.printStackTrace();
        }
        fwd(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("in doGet");
        if (null != request.getSession().getAttribute("order_id")) {
            request.getSession().removeAttribute("order_id");
            LOG.debug("removed orderId attribute");
        }
        doPost(request, response);
        //fwd(request, response);
    }

}
