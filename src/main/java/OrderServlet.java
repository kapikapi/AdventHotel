import org.apache.log4j.Logger;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * Created by Elizaveta Kapitonova on 19.01.16.
 */
@WebServlet(name = "OrderServlet")
public class OrderServlet extends HttpServlet {
    public static final Logger LOG= Logger.getLogger(LoginServlet.class);
    public static final String ORDER_JSP = "/jsp/order.jsp";
    public static final String RESULT_PAGE = "/index.jsp";

    public static final String FORMATTER_PATTERN = "yyyy-MM-dd";

    private static void fwd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(ORDER_JSP).forward(req, resp);
    }

    // get params, give them to Administrator
    //
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int number = Integer.parseInt(request.getParameter("number_people"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTER_PATTERN);
        String date_in = request.getParameter("date_in");
        String date_out = request.getParameter("date_out");
        LocalDate dateIn = LocalDate.parse(date_in, formatter);
        LocalDate dateOut = LocalDate.parse(date_out, formatter);
        String classOfComfort = request.getParameter("class");
        String act = request.getParameter("actionName");
        if (act.equals("order")) {
            LOG.debug("Ordering");
            Administrator administrator;
            try {
                administrator = new Administrator(number, classOfComfort, dateIn, dateOut);
                LOG.debug("Searching must be completed");
                List<String> res = administrator.getRes();
                StringBuilder attr = new StringBuilder();
                for (String s : res) {
                    attr.append(s).append("\n");
                }
                request.setAttribute("result", attr);
                LOG.debug(attr);
                response.sendRedirect(RESULT_PAGE);
            } catch (SQLException e) {
                LOG.debug("Search failed");
                request.setAttribute("search_error", e.getMessage());
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
        fwd(request, response);
    }
}
