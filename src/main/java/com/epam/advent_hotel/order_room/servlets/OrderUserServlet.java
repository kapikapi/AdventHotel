package com.epam.advent_hotel.order_room.servlets;

import com.epam.advent_hotel.apartments.Apartment;
import com.epam.advent_hotel.db.DBHandler;
import com.epam.advent_hotel.order_room.Comment;
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
import java.util.List;

/**
 * Created by Elizaveta Kapitonova on 02.02.16.
 */
@WebServlet(name = "OrderUserServlet")
public class OrderUserServlet extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(OrderUserServlet.class);
    public static final String ORDER_USER_JSP = "/jsp/user_order.jsp";
    public static final String ORDER_USER_PAGE = "/user_order";
    public static final int PER_PAGE = 10;

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(ORDER_USER_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (null != request.getParameter("actionName")) {
            int orderId = (int) request.getSession().getAttribute("order_id");
            String act = request.getParameter("actionName");
            if (act.equals("send_comment")) {
                try {
                    User user = (User) request.getSession().getAttribute("user");
                    DBHandler.getInstance().setOrderComment(orderId, request.getParameter("comment"), user.getUserId());
                } catch (SQLException e) {
                    e.printStackTrace();
                    request.setAttribute("error", true);
                    LOG.error(e.getMessage());
                }
                response.sendRedirect(ORDER_USER_PAGE);
            } else if (act.equals("change_lang")) {
                //fwd(request, response);
                response.sendRedirect(ORDER_USER_PAGE);
            } else {
                fwd(request, response);
            }
        } else {
            fwd(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int orderId;
        if (null != request.getParameter("order_id")) {
            orderId = Integer.parseInt(request.getParameter("order_id"));
        } else {
            orderId = (int) request.getSession().getAttribute("order_id");
        }

        if (null != request.getParameter("actionName")) {
            String act = request.getParameter("actionName");
            if (act.equals("approve")) {
                try {
                    DBHandler.getInstance().setOrderStatus(orderId, OrderStatus.APPROVED);
                    DBHandler.getInstance().setOrderCost(orderId);
                    request.setAttribute("order", DBHandler.getInstance().getOrder(orderId));
                } catch (SQLException e) {
                    request.setAttribute("error", e.getMessage());
                    LOG.error(e.getMessage());
                    e.printStackTrace();
                }

            }
        }

        try {
            Order order = DBHandler.getInstance().getOrder(orderId);
            if (!order.getStatus().equals(OrderStatus.REQUESTED) || !(order.getStatus().equals(OrderStatus.REJECTED))) {
                Apartment apartment = DBHandler.getInstance().getApt(order.getOrderedAptId());
                request.setAttribute("apt", apartment);
            }
            request.getSession().setAttribute("order_id", orderId);
            request.setAttribute("order", order);
            int page = 1;
            if (null != request.getParameter("page")) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            int offset = (page - 1) * PER_PAGE;
            List<Comment> commentList = DBHandler.getInstance().getOrdersComments(orderId, PER_PAGE, offset);
            int numberOfComments = DBHandler.getInstance().getNumberOfComments(orderId);
            int noOfPages = (int) Math.ceil(numberOfComments * 1.0 / PER_PAGE);
            request.setAttribute("noOfPages", noOfPages);
            request.setAttribute("currentPage", page);
            request.setAttribute("comment_list", commentList);
            if (commentList.isEmpty()) {
                request.setAttribute("no_comments", true);
            }

        } catch (SQLException e) {
            request.setAttribute("error", true);
            e.printStackTrace();
            LOG.error(e.getMessage());
        }
        fwd(request, response);
        //doPost(request, response);
    }
}
