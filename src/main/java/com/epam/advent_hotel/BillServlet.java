package com.epam.advent_hotel;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

/**
 * Created by Elizaveta Kapitonova on 22.01.16.
 */
@WebServlet(name = "BillServlet")
public class BillServlet extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(BillServlet.class);
    public static final String BILL_JSP = "/jsp/bill.jsp";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(BILL_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String act = request.getParameter("actionName");
        if (act.equals("bill")) {
            Administrator administrator = new Administrator();
            try {
                int roomId = Integer.parseInt(request.getParameter("room_id"));
                LOG.debug(roomId);
                RoomOrder room = administrator.getRoomById(roomId);
                String res = "Final cost for room №%s %s class from %s to %s for %s people is %s$.";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(OrderServlet.FORMATTER_PATTERN);
                request.setAttribute("res_str", String.format(res, room.getNumber(), room.getClassOfComfort(),
                        room.getDateIn(), room.getDateOut(), room.getPlaces(), room.getCost()));
                request.setAttribute("room", room);
                LOG.debug(room.getNumber());
                UserAccount currentUser = (UserAccount) request.getAttribute("user");
                administrator.setOrder(room.getId(), currentUser, room.getDateIn(), room.getDateOut());
                LOG.debug("order registered");
            } catch (SQLException e) {
                LOG.debug(e.getMessage());
            }
        } else {
            LOG.debug("Showing final bill failed");
            response.getWriter().write("Error occured. Try again.");
            response.getWriter().flush();
        }

        fwd(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        fwd(request, response);
    }
}
