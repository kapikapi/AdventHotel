package com.epam.advent_hotel;

import com.epam.advent_hotel.db.DBHandler;
import com.epam.advent_hotel.users.AccessLevel;
import com.epam.advent_hotel.users.User;
import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Elizaveta Kapitonova on 04.02.16.
 */
public class UsernameTagHandler extends TagSupport {
    private static final Logger LOG = Logger.getLogger(UsernameTagHandler.class);

    private int userId;
    private Locale lang;

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            User user = DBHandler.getInstance().getUser(userId);

            if (user.getAccessLevel().equals(AccessLevel.ADMIN)) {
                String userName = user.getName();
                ResourceBundle resourceBundle = ResourceBundle.getBundle("local", lang);
                String admin = resourceBundle.getString("admin.order_page.admin_name");
                out.print(admin+ " " + userName);
            } else {
                String userLogin = user.getLogin();
                out.print(userLogin);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return SKIP_BODY;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Locale getLang() {
        return lang;
    }

    public void setLang(Locale lang) {
        this.lang = lang;
    }

}
