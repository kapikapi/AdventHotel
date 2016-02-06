package com.epam.advent_hotel.order_room.servlets;

import com.epam.advent_hotel.apartments.Apartment;
import com.epam.advent_hotel.apartments.LocalAptDescription;
import com.epam.advent_hotel.db.DBHandler;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Created by Elizaveta Kapitonova on 02.02.16.
 */
@WebServlet(name = "RoomDetailsServlet")
public class RoomDetailsServlet extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(RoomDetailsServlet.class);

    public static final String ROOM_DETAILS_JSP = "/jsp/room.jsp";
    public static final String ROOM_DETAILS = "/room";
    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(ROOM_DETAILS_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int roomId = Integer.parseInt(request.getPathInfo().substring(1));
        response.sendRedirect(ROOM_DETAILS + "/" + String.valueOf(roomId));
        //fwd(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int orderId = (int) request.getSession().getAttribute("order_id");
            int roomId = Integer.parseInt(request.getPathInfo().substring(1));
            Apartment apartment = DBHandler.getInstance().getApt(roomId);
            Locale locale = (Locale) request.getSession().getAttribute("locale");
            LocalAptDescription description = DBHandler.getInstance().getDescription(apartment.getDescription(), locale);
            //int orderId = Integer.parseInt(request.getParameter("order_id"));
            request.setAttribute("room", apartment);
            request.setAttribute("description", description.getText());
            request.setAttribute("order_id", orderId);
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", true);
            LOG.error(e.getMessage());
        }

        fwd(request, response);
    }
}
