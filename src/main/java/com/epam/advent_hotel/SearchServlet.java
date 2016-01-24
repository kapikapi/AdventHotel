package com.epam.advent_hotel;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Elizaveta Kapitonova on 24.01.16.
 */
@WebServlet(name = "SearchServlet")
public class SearchServlet extends HttpServlet {
    public static final Logger LOG= Logger.getLogger(SearchServlet.class);
    public static final String SEARCH_JSP = "/jsp/search.jsp";
    public static final String BILL_PAGE = "/bill";

    public static final String FORMATTER_PATTERN = "yyyy-MM-dd";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(SEARCH_JSP).forward(req, resp);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
