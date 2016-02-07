package com.epam.advent_hotel.users;

import com.epam.advent_hotel.db.DBHandler;
import com.epam.advent_hotel.order_room.Order;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet displays list of all users orders.
 *
 * @author Elizaveta Kapitonova
 */
@WebServlet(name = "UserServlet")
public class UserServlet extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(UserServlet.class);
    public static final String USER_JSP = "/jsp/user.jsp";
    public static final String USER_PAGE = "/user";
    public static final String ORDER_JSP = "/order";
    private static final int PER_PAGE = 10;

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(USER_JSP).forward(req, resp);
    }

    /**
     * Gets list of all orders of user by pages to display.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (null != request.getParameter("actionName")) {
            String act = request.getParameter("actionName");
            if (act.equals("newOrder")) {
                response.sendRedirect(ORDER_JSP);
            } else {
                response.sendRedirect(USER_PAGE);
            }
        } else {
            try {
                User currentUser = (User) request.getSession().getAttribute("user");
                request.setAttribute("user_name", currentUser.getName());
                request.setAttribute("user_login", currentUser.getLogin());
                int page = 1;
                if (null != request.getParameter("page")) {
                    page = Integer.parseInt(request.getParameter("page"));
                }
                int offset = (page - 1) * PER_PAGE;
                List<Order> resList = DBHandler.getInstance().getUsersOrders(currentUser.getUserId(), offset, PER_PAGE);
                int numberOfOrders = DBHandler.getInstance().getUsersNumberOfOrders(currentUser.getUserId());
                int noOfPages = (int) Math.ceil(numberOfOrders*1.0/PER_PAGE);
                List<Order> newList = new ArrayList<>();
                List<Order> oldList = new ArrayList<>();
                for (Order r : resList) {
                    long p = ChronoUnit.DAYS.between(LocalDate.now(), r.getDateOut());
                    if (p>=0) {
                        newList.add(r);
                    } else {
                        oldList.add(r);
                    }
                }
                request.setAttribute("new_orders_list", newList);
                request.setAttribute("old_orders_list", oldList);
                request.setAttribute("noOfPages", noOfPages);
                request.setAttribute("currentPage", page);
                if (resList.isEmpty()) {
                    //String noOrders = "No rooms have been ordered yet";
                    request.setAttribute("no_result", true);
                }
                fwd(request, response);
            } catch (SQLException e) {
                request.setAttribute("error", true);
                e.printStackTrace();
                LOG.error(e.getMessage());

            } catch (NullPointerException e) {
                e.printStackTrace();
                fwd(request, response);
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (null != request.getSession().getAttribute("order_id")) {
            request.getSession().removeAttribute("order_id");
        }
        doPost(request, response);
        //fwd(request, response);
    }
}
