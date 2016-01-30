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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Created by Elizaveta Kapitonova on 26.01.16.
 */
@WebServlet(name = "EditOrderServlet")
public class EditOrderServlet extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(EditOrderServlet.class);
    public static final String EDIT_JSP = "/jsp/edit_order.jsp";
    public static final String USER_JSP = "/user";
    public static final String SEARCH_PAGE = "/search";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(EDIT_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Administrator administrator = new Administrator();
        String act = request.getParameter("actionName");
        int orderId = Integer.parseInt(request.getParameter("order_id"));

        if (act.equals("editOrder")) {
            request.setAttribute("order_id", orderId);
            try {
                RoomOrder room = administrator.getOrderById(orderId);
                //RoomOrder roomOrder = DatabaseHandler.getRoomOrder()
                LOG.debug("edit order");
                LOG.debug(orderId);
                LOG.debug(room.getNumber());
                LOG.debug(room.getDateIn());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(OrderServlet.FORMATTER_PATTERN);
                String dateInFormatted = formatter.format(room.getDateIn());
                String dateOutFormatted = formatter.format(room.getDateOut());
                request.setAttribute("room_places", room.getPlaces());
                request.setAttribute("dateIn", dateInFormatted);
                request.setAttribute("dateOut", dateOutFormatted);

                fwd(request, response);
            } catch (SQLException e) {
                LOG.debug(e.getMessage());
                request.setAttribute("search_error", e.getMessage());
                fwd(request, response);
            }

        } else if (act.equals("submit_edit")) {
            LOG.debug("submit edit");
            request.setAttribute("order_id", orderId);
            int numberPeople = Integer.parseInt(request.getParameter("number_people"));
            DateTimeFormatter formatter = DateTimeFormatter.
                    ofPattern(OrderServlet.FORMATTER_PATTERN);
            String date_in = request.getParameter("date_in");
            String date_out = request.getParameter("date_out");
            LocalDate dateIn = LocalDate.parse(date_in, formatter);
            LocalDate dateOut = LocalDate.parse(date_out, formatter);
            int classOfComfort = Integer.parseInt(request.getParameter("class"));
            try {
                LOG.debug("Searching must be completed");
                List<RoomOrder> res = administrator.getRes(numberPeople, classOfComfort, dateIn, dateOut);
                RoomOrder currRoom = administrator.getRoomByOrderId(orderId);
                LOG.debug(currRoom.getNumber());
                LOG.debug(administrator.isRoomAvailible(orderId, dateIn, dateOut));
                boolean currRoomAvailable = administrator.isRoomAvailible(orderId, dateIn, dateOut);
                if ((numberPeople == currRoom.getPlaces()) && (classOfComfort == currRoom.getClassOfComfort())
                        && currRoomAvailable) {
                    LOG.debug("Can have same room");

                    int p = (int) ChronoUnit.DAYS.between(dateIn, dateOut);
                    currRoom.setCost(p*currRoom.getCost());
                    //res.add(0, currRoom);
                    request.setAttribute("curr_room", currRoom);
                    String msg = "This is the room from your order under edit. You can also order it now.";
                    request.setAttribute("prev_room", msg);
                }

                request.setAttribute("result_list", res);
                if (res.isEmpty() && !currRoomAvailable) {
                    request.setAttribute("no_result", "Editing your order with this parameters can not be done. " +
                            "Rooms searching gives no result. Please, try again.");
                    LOG.debug("No result");
                    fwd(request, response);
                } else {
                    request.getSession().setAttribute("order_id", orderId);
                    request.getSession().setAttribute("date_in", dateIn);
                    request.getSession().setAttribute("date_out", dateOut);
                    request.getRequestDispatcher(SEARCH_PAGE).forward(request, response);
                }
            } catch (SQLException e) {
                LOG.debug("Search failed");
                request.setAttribute("search_error", e.getMessage());
                fwd(request, response);
            }

        }
        else {
            response.getWriter().write("Error occurred");
            response.getWriter().flush();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        fwd(request, response);
    }
}
