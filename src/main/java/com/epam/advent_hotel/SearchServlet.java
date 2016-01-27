package com.epam.advent_hotel;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by Elizaveta Kapitonova on 24.01.16.
 */
@WebServlet(name = "SearchServlet")
public class SearchServlet extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(SearchServlet.class);
    public static final String SEARCH_JSP = "/jsp/search.jsp";
    public static final String BILL_PAGE = "/bill";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(SEARCH_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        int number = Integer.parseInt(request.getParameter("number_people"));
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTER_PATTERN);
//        String date_in = request.getParameter("date_in");
//        String date_out = request.getParameter("date_out");
//        LocalDate dateIn = LocalDate.parse(date_in, formatter);
//        LocalDate dateOut = LocalDate.parse(date_out, formatter);
//        int classOfComfort = Integer.parseInt(request.getParameter("class"));
        String act = request.getParameter("actionName");
        if (act.equals("search")) {
            List<RoomOrder> resList = (List<RoomOrder>) request.getAttribute("result_list");
            for (RoomOrder r : resList) {
                LOG.debug("el");
                LOG.debug(r.getNumber());
            }
            if (resList.isEmpty()) {
                LOG.debug("comes empty list");
            }
            request.setAttribute("result_list", resList);
            response.sendRedirect(BILL_PAGE);
        } else {
            fwd(request, response);
//            LOG.debug("Showing search results totally failed");
//            response.getWriter().write("Error occurred");
//            response.getWriter().flush();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        fwd(request, response);
    }
}
