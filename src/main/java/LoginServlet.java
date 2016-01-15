
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

    public static final String LOGIN_JSP = "/jsp/login.jsp";
    public static final String START_PAGE = "/index.jsp";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(LOGIN_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String act = req.getParameter("action");
        if (act.equals("authentication")) {
            try {
                UserAccountClass user = new UserAccountClass(login, password);
                req.getSession().setAttribute("user", user);
                resp.getWriter().write("Success");
                req.setAttribute("auth_success", true);
            } catch (LoginException e) {
                req.setAttribute("auth_success", false);
                resp.getWriter().write("Authentication failed");
            }
        }
        else {
            resp.getWriter().write("Error occurred");
            resp.getWriter().flush();
        }


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        UserAccountClass user = (UserAccountClass) req.getSession().getAttribute("user");
        if (user != null) {
            resp.sendRedirect(START_PAGE);
            return;
        }
        fwd(req, resp);
    }

}
