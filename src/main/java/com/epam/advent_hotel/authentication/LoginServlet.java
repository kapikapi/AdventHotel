package com.epam.advent_hotel.authentication;

import com.epam.advent_hotel.users.AccessLevel;
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
 * Created by Elizaveta Kapitonova on 13.01.16.
 */
public class LoginServlet extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(LoginServlet.class);

    public static final String LOGIN_JSP = "/jsp/login.jsp";
    public static final String USER_JSP = "/user";
    public static final String ADMIN_JSP = "/admin";

    private static final String PROPERTY = "local";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(LOGIN_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String act = req.getParameter("actionName");
        switch (act) {
            case "authentication":
                try {
                    User user = new User(login);
                    user = user.logIn(login, password);
                    req.getSession().setAttribute("user", user);
                    if (user.getAccessLevel().equals(AccessLevel.ADMIN)) {
                        req.getSession().setAttribute("isAdmin", true);
                    }
                    LOG.debug("Auth correct");
                    if (user.getAccessLevel().equals(AccessLevel.ADMIN)) {
                        resp.sendRedirect(ADMIN_JSP);
                    } else {
                        resp.sendRedirect(USER_JSP);
                    }


                } catch (LoginException e) {
                    LOG.debug("Auth failed");
                    String error = getLocMsg(req, PROPERTY).getString("login.login_error");
                    req.setAttribute("auth_error", error);

                    fwd(req, resp);

                }
                break;
            case "logout":
                req.getSession().invalidate();
                fwd(req, resp);
                break;
            default:
                fwd(req, resp);
                break;
        }


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Locale locale = (Locale) req.getSession().getAttribute("locale");
        if (locale == null) {
            req.getSession().setAttribute("locale", new Locale("en"));
        }
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            if (user.getAccessLevel().equals(AccessLevel.ADMIN)) {
                resp.sendRedirect(ADMIN_JSP);
            } else {
                resp.sendRedirect(USER_JSP);
            }
        } else {
            fwd(req, resp);
        }

    }
    private ResourceBundle getLocMsg(HttpServletRequest request, String propertyName) {
        Locale locale = (Locale) request.getSession().getAttribute("locale");
        return ResourceBundle.getBundle(propertyName, locale);
    }


}
