package com.epam.advent_hotel.admin;

import com.epam.advent_hotel.db.DBHandler;
import com.epam.advent_hotel.users.User;
import org.apache.log4j.Logger;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Servlet gets list of all users (by page) and can set a user as admin.
 *
 * @author Elizaveta Kapitonova
 */
@WebServlet(name = "UsersListServlet")
public class UsersListServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(UsersListServlet.class);
    private static final String USER_LIST_JSP = "/jsp/admin/users_list.jsp";
    private static final String USER_LIST_PAGE = "users_list";
    private static final int PER_PAGE = 10;
    private static final String PROPERTY = "local";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(USER_LIST_JSP).forward(req, resp);
    }

    /**
     * Gets users list and if administrator sets some user as new administrator, changes user's access level.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int page = 1;
            if (null != request.getParameter("page")) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            int offset = (page - 1) * PER_PAGE;
            List<User> usersList = DBHandler.getInstance().getAllUsers(PER_PAGE, offset);
            int numberOfOrders = DBHandler.getInstance().getUsersNumber();
            int noOfPages = (int) Math.ceil(numberOfOrders * 1.0 / PER_PAGE);
            request.setAttribute("noOfPages", noOfPages);
            request.setAttribute("currentPage", page);
            request.setAttribute("users_list", usersList);

        } catch (SQLException | LoginException e) {
            request.setAttribute("error", true);
            e.printStackTrace();
            LOG.error(e.getMessage());
        }
        if (null != request.getParameter("actionName")) {
            String act = request.getParameter("actionName");
            if (act.equals("set_admin")) {
                int userId = Integer.parseInt(request.getParameter("user_id"));
                try {
                    int res = DBHandler.getInstance().setUserAdmin(userId);
                    if (res == 0) {
                        Locale locale = (Locale) request.getSession().getAttribute("locale");
                        ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTY, locale);
                        String logErr = resourceBundle.getString("users.set_admin.log");
                        LOG.error(logErr + " " + String.valueOf(res));
                        request.setAttribute("error", true);
                        fwd(request, response);
                    } else {
                        response.sendRedirect(USER_LIST_PAGE);
                    }
                } catch (SQLException e) {
                    request.setAttribute("error", true);
                    e.printStackTrace();
                    LOG.error(e.getMessage());
                }

            } else {
                fwd(request, response);
            }

        } else {
            fwd(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}
