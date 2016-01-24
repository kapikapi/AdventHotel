package com.epam.advent_hotel;

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
import java.util.List;

/**
 * Created by Elizaveta Kapitonova on 19.01.16.
 */
@WebServlet(name = "OrderServlet")
public class OrderServlet extends HttpServlet {
    public static final Logger LOG= Logger.getLogger(OrderServlet.class);
    public static final String ORDER_JSP = "/jsp/order.jsp";
    public static final String BILL_PAGE = "/bill";

    public static final String FORMATTER_PATTERN = "yyyy-MM-dd";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(ORDER_JSP).forward(req, resp);
    }

    // get params, give them to Administrator
    //
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int number = Integer.parseInt(request.getParameter("number_people"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTER_PATTERN);
        String date_in = request.getParameter("date_in");
        String date_out = request.getParameter("date_out");
        LocalDate dateIn = LocalDate.parse(date_in, formatter);
        LocalDate dateOut = LocalDate.parse(date_out, formatter);
        int classOfComfort = Integer.parseInt(request.getParameter("class"));
        String act = request.getParameter("actionName");
        if (act.equals("order")) {
            LOG.debug("Ordering");
            Administrator administrator;
            try {
                administrator = new Administrator();
                LOG.debug("Searching must be completed");
                List<RoomOrder> res = administrator.getRes(number, classOfComfort, dateIn, dateOut);
                request.setAttribute("result_list", res);
                if (res.isEmpty()) {
                    request.setAttribute("no_result", "No results for your search parameters. Please, try again.");
                    LOG.debug("No result");
                }
            } catch (SQLException e) {
                LOG.debug("Search failed");
                request.setAttribute("search_error", e.getMessage());

            }
            fwd(request, response);
        }
        else if(act.equals("chooseRoom")) {
            LOG.debug("Choosing");
//            Order order = new Order();
//            order.setDateIn(dateIn);
//            order.setDateOut(dateOut);
//            // in setRoom return administrator.getRoomById
//            try {
//                order.setRoomById(Integer.parseInt(request.getParameter("room_id")));
//            } catch (SQLException e) {
//                LOG.debug(e.getMessage());
//            }
//            request.setAttribute("order", order);
            response.sendRedirect(BILL_PAGE);
        }
        else {
            LOG.debug("Reg totally failed");
            response.getWriter().write("Error occurred");
            response.getWriter().flush();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        fwd(request, response);
    }
}