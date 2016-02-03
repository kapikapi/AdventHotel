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
import java.util.List;

/**
 * Created by Elizaveta Kapitonova on 02.02.16.
 */
@WebServlet(name = "SearchRoomServlet")
public class SearchRoomServlet extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(SearchRoomServlet.class);
    public static final String SEARCH_ROOM_JSP = "/jsp/search_room.jsp";
    private static final int PER_PAGE = 10;

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(SEARCH_ROOM_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Order order = (Order) request.getAttribute("order");
        LOG.debug("in doPost");
        try {
            int orderId = (int) request.getSession().getAttribute("order_id");
            //int orderId = Integer.parseInt(request.getParameter("order_id"));
            //LOG.debug(orderId);
            //Order order = DBHandler.getInstance().getOrder(orderId);
            if (null != request.getParameter("actionName")) {

                String act = request.getParameter("actionName");
                LOG.debug(act);
                if (act.equals("find_room")) {
                    int page = 1;
                    if (null != request.getParameter("page")) {
                        page = Integer.parseInt(request.getParameter("page"));
                    }
                    int offset = (page - 1) * PER_PAGE;

                    List<Apartment> res = DBHandler.getInstance().getSuitableApts(orderId,
                            PER_PAGE, offset);
                    int numberOfOrders = DBHandler.getInstance().getAptNumbers(orderId);
                    int noOfPages = (int) Math.ceil(numberOfOrders * 1.0 / PER_PAGE);
                    request.setAttribute("noOfPages", noOfPages);
                    request.setAttribute("currentPage", page);
                    for (Apartment apt : res) {
                        LOG.debug(apt.getNumber());
                    }
                    request.setAttribute("result_list", res);

                    if (res.isEmpty()) {
                        request.setAttribute("no_result", "No results for such search parameters. Please, try again.");
                        LOG.debug("No result");

                    }
                    //request.setAttribute("order_id", orderId);
                    // request.setAttribute("order", order);
                    fwd(request, response);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOG.debug(e.getMessage());
            request.setAttribute("error", e.getMessage());
            fwd(request, response);
        }

        //List<RoomOrder> res = DatabaseHandler.getRoomsByParams(number, classOfComfort, dateIn, dateOut);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("in doGet");
        //fwd(request, response);
        doPost(request, response);
    }
}
