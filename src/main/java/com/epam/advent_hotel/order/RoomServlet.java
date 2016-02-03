package com.epam.advent_hotel.order;

import com.epam.advent_hotel.RoomOrder;
import com.epam.advent_hotel.db.DatabaseHandler;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Elizaveta Kapitonova on 23.01.16.
 */
@WebServlet(name = "RoomServlet")
public class RoomServlet extends HttpServlet {
    public static final Logger LOG= Logger.getLogger(RoomServlet.class);
    public static final String ROOM_JSP = "/jsp/room.jsp";
    public static final String BILL_PAGE = "/bill";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(ROOM_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(BILL_PAGE).forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            int roomId = Integer.parseInt(request.getPathInfo().substring(1));
            //Administrator administrator = new Administrator();
            RoomOrder room = DatabaseHandler.getRoomById(roomId);
            LOG.debug(room.getId());
            request.setAttribute("room", room);
        } catch (SQLException | NumberFormatException e) {
            LOG.debug(e.getMessage());
        }

        fwd(request, response);
    }
}