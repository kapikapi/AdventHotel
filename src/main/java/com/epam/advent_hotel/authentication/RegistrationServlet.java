package com.epam.advent_hotel.authentication;

import com.epam.advent_hotel.UserAccount;
import com.epam.advent_hotel.users.User;
import org.apache.log4j.Logger;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Elizaveta Kapitonova on 14.01.16.
 */
public class RegistrationServlet extends HttpServlet {
    public static final Logger LOG= Logger.getLogger(RegistrationServlet.class);

    public static final String REGISTRATION_JSP = "/jsp/registration.jsp";
    public static final String LOGIN_JSP = "/authentication";
    public static final String START_PAGE = "/index.jsp";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(REGISTRATION_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String act = request.getParameter("actionName");
        if (act.equals("registration")) {
            LOG.debug("Registrating");
            try {
                LOG.debug(name);
                User user = new User(name, login, password, email);
                LOG.debug("Reg must be completed");
                request.getSession().setAttribute("user", user.logIn(login, password));
                response.sendRedirect(LOGIN_JSP);

            } catch (LoginException e) {
                LOG.debug("Reg failed");
                request.setAttribute("reg_error", e.getMessage());
                fwd(request, response);
            }

        }
        else {
            LOG.debug("Reg totally failed");
            response.getWriter().write("Error occurred");
            response.getWriter().flush();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            response.sendRedirect(START_PAGE);
            return;
        }
        fwd(request, response);
    }

}
