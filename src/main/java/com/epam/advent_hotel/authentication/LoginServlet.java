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
 * Servlet logs user in if login and password are correct
 *
 * @author Elizaveta Kapitonova
 */
public class LoginServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(LoginServlet.class);

    private static final String LOGIN_JSP = "/jsp/login.jsp";
    private static final String USER_JSP = "/user";
    private static final String ADMIN_JSP = "/admin";

    private static final String PROPERTY = "local";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(LOGIN_JSP).forward(req, resp);
    }

    /**
     * Gets User object and sets it as session attribute.
     * Redirects to user or admin home page if authentication was successful.
     * Removes session's user attribute if user logs out
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
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
                    if (user.getAccessLevel().equals(AccessLevel.ADMIN)) {
                        resp.sendRedirect(ADMIN_JSP);
                    } else {
                        resp.sendRedirect(USER_JSP);
                    }


                } catch (LoginException e) {
                    Locale locale = (Locale) req.getSession().getAttribute("locale");
                    ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTY, locale);
                    String error = resourceBundle.getString("login.login_error");
                    req.setAttribute("auth_error", error);
                    LOG.info(error);
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

    /**
     * Redirects from authentication page to user's or admin's page if user is logged in
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
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

}
