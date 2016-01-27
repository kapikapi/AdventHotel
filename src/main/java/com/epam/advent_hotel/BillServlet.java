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

/**
 * Created by Elizaveta Kapitonova on 22.01.16.
 */
@WebServlet(name = "BillServlet")
public class BillServlet extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(BillServlet.class);
    public static final String BILL_JSP = "/jsp/bill.jsp";
    public static final String USER_PAGE = "/user";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(BILL_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("bill");
        String act = request.getParameter("actionName");
        int roomId = Integer.parseInt(request.getParameter("room_id"));
        LOG.debug(roomId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(OrderServlet.FORMATTER_PATTERN);
//        String date_in = request.getParameter("date_in");
//        String date_out = request.getParameter("date_out");
        LocalDate dateIn = (LocalDate) request.getSession().getAttribute("date_in");
        LocalDate dateOut = (LocalDate) request.getSession().getAttribute("date_out");
        Administrator administrator = new Administrator();
        RoomOrder room = null;
        try {
            room = administrator.getRoomOrder(roomId, dateIn, dateOut);
        } catch (SQLException e) {
            LOG.debug("Can not get such RoomOrder");
            response.getWriter().write("Error occured. Try again.");
            response.getWriter().flush();
        }
        if (null!=request.getSession().getAttribute("user")) {
            if (act.equals("search")) {
                LOG.debug("after search");
                request.setAttribute("room_id", roomId);
                LOG.debug(roomId);
//                request.setAttribute("date_in", date_in);
//                request.setAttribute("date_out", date_out);
                String res = "Final cost for room №%s %s class from %s to %s for %s people is %s$.";
                request.setAttribute("res_str", String.format(res, room.getNumber(), room.getClassOfComfort(),
                        room.getDateIn(), room.getDateOut(), room.getPlaces(), room.getCost()));
                fwd(request, response);
            } else if (act.equals("bill")) {
                LOG.debug("bill making");

                try {
                    LOG.debug(roomId);
                    //request.setAttribute("room", room);

                    UserAccount currentUser = (UserAccount) request.getSession().getAttribute("user");

                    LOG.debug(currentUser.getLogin());

                    administrator.setOrder(room.getId(), currentUser, room.getDateIn(), room.getDateOut());
                    LOG.debug("order registered");
                    request.getSession().removeAttribute("date_in");
                    request.getSession().removeAttribute("date_out");
                    response.sendRedirect(USER_PAGE);

                } catch (SQLException e) {
                    LOG.debug(e.getMessage());
                    request.setAttribute("setError", "Error while registering your order. Try again.");
                    fwd(request, response);
                }
                //fwd(request, response);
            } else {
                LOG.debug("Showing final bill failed");
                response.getWriter().write("Error occured. Try again.");
                response.getWriter().flush();
            }
        } else {
            fwd(request, response);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        fwd(request, response);
    }
}
