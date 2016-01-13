
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
    public static final String START_PAGE = "/start";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(LOGIN_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

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
