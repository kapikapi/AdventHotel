package com.epam.advent_hotel.order_room.servlets;

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
@WebServlet(name = "OrderAdminServlet")
public class OrderAdminServlet extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(OrderAdminServlet.class);
    public static final String ORDER_ADMIN_JSP = "/jsp/admin_order.jsp";
    public static final String ORDER_ADMIN_PAGE = "/admin_order";


    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(ORDER_ADMIN_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("in doPost");
        if (null != request.getParameter("actionName")) {
            String act = request.getParameter("actionName");
            LOG.debug(act);
            if (act.equals("find_room")) {
                //request.getRequestDispatcher(SEARCH_ROOM_PAGE).forward(request, response);
                fwd(request, response);
            } else if (act.equals("send_comment")) {
                try {
                    int orderId = (int) request.getSession().getAttribute("order_id");
                    User user = (User) request.getSession().getAttribute("user");
                    DBHandler.getInstance().setOrderComment(orderId, request.getParameter("comment"), user.getUserId());
                } catch (SQLException e) {
                    e.printStackTrace();
                    LOG.debug(e.getMessage());
                    request.setAttribute("error", e.getMessage());
                }
                //fwd(request, response);
                response.sendRedirect(ORDER_ADMIN_PAGE);
            }
        } else {

            fwd(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("in doGet");
        int orderId;
        LOG.debug(request.getParameter("order_id"));

        try {
            Order order;
            if (null != request.getParameter("room_id")) {
                LOG.debug("have room_id");
                orderId = (int) request.getSession().getAttribute("order_id");
                int aptId = Integer.parseInt(request.getParameter("room_id"));
                DBHandler.getInstance().setOrdersApt(orderId, aptId);
                DBHandler.getInstance().setOrderStatus(orderId, OrderStatus.IN_DISCUSSION);
            } else if (null == request.getSession().getAttribute("order_id")){
                LOG.debug("don't have order_id in session");
                orderId = Integer.parseInt(request.getParameter("order_id"));
                request.getSession().setAttribute("order_id", orderId);
            } else {
                LOG.debug("have order_id but don't have  room_id");
                orderId = (int) request.getSession().getAttribute("order_id");
            }
            order = DBHandler.getInstance().getOrder(orderId);
            request.setAttribute("order", order);
            int page = 1;
            if (null != request.getParameter("page")) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            int offset = (page - 1) * OrderUserServlet.PER_PAGE;
            List<Comment> commentList = DBHandler.getInstance().getOrdersComments(orderId, OrderUserServlet.PER_PAGE, offset);
            int numberOfComments = DBHandler.getInstance().getNumberOfComments(orderId);
            int noOfPages = (int) Math.ceil(numberOfComments * 1.0 / OrderUserServlet.PER_PAGE);
            request.setAttribute("noOfPages", noOfPages);
            request.setAttribute("currentPage", page);
            request.setAttribute("comment_list", commentList);
            if (commentList.isEmpty()) {
                request.setAttribute("no_comments", true);
            }
        } catch (SQLException e) {
            request.setAttribute("error", e.getMessage());
            e.printStackTrace();
        }
        fwd(request, response);
    }
}
