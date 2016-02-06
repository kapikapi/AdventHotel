package com.epam.advent_hotel.authentication;

import com.epam.advent_hotel.users.User;
import org.apache.log4j.Logger;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Elizaveta Kapitonova on 14.01.16.
 */
public class RegistrationServlet extends HttpServlet {
    public static final Logger LOG= Logger.getLogger(RegistrationServlet.class);

    public static final String REGISTRATION_JSP = "/jsp/registration.jsp";
    public static final String LOGIN_JSP = "/authentication";
    public static final String START_PAGE = "/index.jsp";

    private static final String PROPERTY = "local";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(REGISTRATION_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceBundle resourceBundle = getLocMsgs(request, PROPERTY);
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String act = request.getParameter("actionName");
        if (act.equals("registration")) {
            try {
                User user = new User(name, login, password, email);
                request.getSession().setAttribute("user", user.logIn(login, password));
                response.sendRedirect(LOGIN_JSP);

            } catch (LoginException e) {
                String errorMsg;
                if (e.getMessage().equals("1")) {
                    errorMsg = resourceBundle.getString("registration.registration_error.already_exists");
                } else {
                    errorMsg = resourceBundle.getString("registration.registration_error.wrong_email");
                }
                request.setAttribute("reg_error", errorMsg);
                LOG.info(errorMsg);
                fwd(request, response);
            }

        }
        else {
            fwd(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Locale locale = (Locale) request.getSession().getAttribute("locale");
//        if (locale == null) {
//            request.getSession().setAttribute("locale", new Locale("en"));
//        }
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            response.sendRedirect(START_PAGE);
            return;
        }

        fwd(request, response);
    }

    private ResourceBundle getLocMsgs(HttpServletRequest request, String propertyName) {
        Locale locale = (Locale) request.getSession().getAttribute("locale");
        return ResourceBundle.getBundle(propertyName, locale);
    }

}
