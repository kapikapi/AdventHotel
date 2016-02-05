package com.epam.advent_hotel.authentication;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elizaveta Kapitonova on 31.01.16.
 */
@WebFilter(filterName = "AuthenticationFilter")
public class AuthenticationFilter implements Filter {
    public static final Logger LOG= Logger.getLogger(AuthenticationFilter.class);
    private static final String AUTH_PAGE = "/auth_error.jsp";
    private static final String LOGIN_PAGE = "/authentication";
    private static final String REG_PAGE = "/registration";
    private static final String START_PAGE = "/";
    private static final String INDEX_PAGE = "/index";
    List<String> noRegNeeded;


    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(false);
        String URI = request.getRequestURI();
        LOG.debug(URI);

        if (!noRegNeeded.contains(URI) && (session == null || session.getAttribute("user") == null)) {
            //request.getRequestDispatcher(AUTH_PAGE).forward(request, response);
            response.sendRedirect(AUTH_PAGE); // No logged-in user found
        } else {
            chain.doFilter(req, resp); // Logged-in user found
        }
    }

    public void init(FilterConfig config) throws ServletException {
        noRegNeeded = new ArrayList<>(5);
        noRegNeeded.add(START_PAGE);
        noRegNeeded.add(REG_PAGE);
        noRegNeeded.add(LOGIN_PAGE);
        noRegNeeded.add(AUTH_PAGE);
        noRegNeeded.add(INDEX_PAGE);
    }

}
