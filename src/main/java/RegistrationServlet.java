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
    public static final String START_PAGE = "/index.jsp";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(REGISTRATION_JSP).forward(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String act = request.getParameter("actionName");
        if (act.equals("registration")) {
            LOG.debug("Registrating");
            request.setAttribute("reg_error", false);
            try {
                UserAccountClass user = new UserAccountClass(login, password, email);
                LOG.debug("Reg must be completed");
                request.getSession().setAttribute("user", user);
                response.getWriter().write("Success");

            } catch (LoginException e) {
                LOG.debug("Reg failed");
                request.setAttribute("reg_error", true);
                response.getWriter().write("Registration failed");
            }
        }
        else {
            LOG.debug("Reg totally failed");
            response.getWriter().write("Error occurred");
            response.getWriter().flush();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccountClass user = (UserAccountClass) request.getSession().getAttribute("user");
        if (user != null) {
            response.sendRedirect(START_PAGE);
            return;
        }
        fwd(request, response);
    }

}
