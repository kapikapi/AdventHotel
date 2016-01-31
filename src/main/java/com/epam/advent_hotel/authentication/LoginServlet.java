package com.epam.advent_hotel.authentication;

import com.epam.advent_hotel.UserAccount;
import org.apache.log4j.Logger;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Elizaveta Kapitonova on 13.01.16.
 */
public class LoginServlet extends HttpServlet {
    public static final Logger LOG= Logger.getLogger(LoginServlet.class);

    public static final String LOGIN_JSP = "/jsp/login.jsp";
    public static final String USER_JSP = "/user";

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
        if (act.equals("authentication")) {
            try {

                UserAccount user = new UserAccount(login, password);
                req.getSession().setAttribute("user", user);
                LOG.debug("Auth correct");
                //resp.getWriter().write("Success");
                resp.sendRedirect(USER_JSP);

            } catch (LoginException e) {
                LOG.debug("Auth failed");
                req.setAttribute("auth_error", e.getMessage());
                LOG.debug(req.getAttribute("auth_error"));
                //resp.sendRedirect("/authentication");

                fwd(req, resp);

            }
        } else if (act.equals("logout")) {
            req.getSession().invalidate();
            fwd(req, resp);
        }
        else {
            resp.getWriter().write("Error occurred");
            resp.getWriter().flush();
        }


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        UserAccount user = (UserAccount) req.getSession().getAttribute("user");
        if (user != null) {
            resp.sendRedirect(USER_JSP);
            return;
        }
        fwd(req, resp);
    }

}
