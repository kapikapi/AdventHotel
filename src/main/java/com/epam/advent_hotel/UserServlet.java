package com.epam.advent_hotel;

import com.epam.advent_hotel.db.DatabaseHandler;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elizaveta Kapitonova on 25.01.16.
 */
@WebServlet(name = "UserServlet")
public class UserServlet extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(UserServlet.class);
    public static final String USER_JSP = "/jsp/user.jsp";
    public static final String ORDER_JSP = "/order";
    //public static final String REMOVE_JSP = "/remove_warning";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(USER_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("in doPost");

        if (null != request.getParameter("actionName")) {
            String act = request.getParameter("actionName");
            if (act.equals("newOrder")) {
                LOG.debug("new order");
                response.sendRedirect(ORDER_JSP);
            }
//            else if (act.equals("removeOrder")) {
//                response.sendRedirect(REMOVE_JSP);
//            }
        } else {
            try {
                UserAccount currentUser = (UserAccount) request.getSession().getAttribute("user");
                request.setAttribute("user_login", currentUser.getLogin());
                Administrator administrator = new Administrator();
                List<RoomOrder> resList = administrator.getUsersOrders(currentUser);
                List<RoomOrder> newList = new ArrayList<>();
                List<RoomOrder> oldList = new ArrayList<>();
                for (RoomOrder r : resList) {
                    long p = ChronoUnit.DAYS.between(LocalDate.now(), r.getDateIn());
                    if (p>=0) {
                        newList.add(r);
                    } else {
                        oldList.add(r);
                    }
                }
                request.setAttribute("new_orders_list", newList);
                request.setAttribute("old_orders_list", oldList);
                for (RoomOrder r : resList) {
                    LOG.debug(r.getOrderId());
                }
                LOG.debug("Got orders list");
                if (resList.isEmpty()) {
                    String noOrders = "No rooms have been ordered yet";
                    LOG.debug("No orders");
                    request.setAttribute("no_result", noOrders);
                }
                fwd(request, response);
            } catch (SQLException e) {
                LOG.debug("Getting users list of orders failed" + e.getMessage());
            } catch (NullPointerException e) {
                LOG.debug("not authorized user");
                fwd(request, response);
            }
        }

//        }  else {
//            response.getWriter().write("Error occurred");
//            response.getWriter().flush();
//        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("in doGet");
        doPost(request, response);
        // fwd(request, response);
    }
}
