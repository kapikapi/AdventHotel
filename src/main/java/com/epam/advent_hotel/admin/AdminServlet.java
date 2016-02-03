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

/**
 * Created by Elizaveta Kapitonova on 01.02.16.
 */
@WebServlet(name = "AdminServlet")
public class AdminServlet extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(AdminServlet.class);
    public static final String ADMIN_JSP = "/jsp/admin.jsp";
    public static final String ORDER_JSP = "/order";

    private static final int PER_PAGE = 10;

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(ADMIN_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Order> ordersList;
        int page = 1;
        if (null != request.getParameter("page")) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        int offset = (page - 1) * PER_PAGE;
        int numberOfOrders;
        OrderStatus status = null;
        String heading = "All orders";
        if (null != request.getParameter("actionName")) {
            String act = request.getParameter("actionName");
            LOG.debug(act);
            switch (act) {
                case "requested":
                    status = OrderStatus.REQUESTED;
                    heading = "Requested orders";
                    break;
                case "in_discussion":
                    status = OrderStatus.IN_DISCUSSION;
                    heading = "Orders in discussion";
                    break;
                case "approved":
                    status = OrderStatus.APPROVED;
                    heading = "Approved orders";
                    break;
                case "paid":
                    status = OrderStatus.PAID;
                    heading = "Paid orders";
                    break;
                case "rejected":
                    status = OrderStatus.REJECTED;
                    heading = "Rejected orders";
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
                request.setAttribute("no_result", "No such orders");
            }
        } catch (SQLException e) {
            request.setAttribute("no_result", "No such orders");
            request.setAttribute("error", e.getMessage());
            e.printStackTrace();
        }
        fwd(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (null != request.getSession().getAttribute("order_id")) {
            request.getSession().removeAttribute("order_id");
        }
        doPost(request, response);
        //fwd(request, response);
    }
}