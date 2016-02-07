package com.epam.advent_hotel.users;

import com.epam.advent_hotel.db.DBHandler;
import org.apache.log4j.Logger;

import javax.security.auth.login.LoginException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple User entity with getters and setters.
 *
 * @author Elizaveta Kapitonova
 */
public class User {
    public static final Logger LOG= Logger.getLogger(User.class);

    private int userId;
    private String login;
    private String email;
    private AccessLevel accessLevel;
    private String name;

    // log in
    public User(String login)
            throws LoginException {
        this.login = login;
    }

    // 3 arguments mean that it is registration
    public User(String name, String login, String password, String email)
            throws LoginException{
        this.login = login;
        if (checkEmail(email)) {
            boolean completed = DBHandler.getInstance().register(name, this.login, hash(password), email);
            if (!completed) {
                throw new LoginException(String.valueOf(1));
            }
        }
        else {
            throw new LoginException(String.valueOf(0));
        }
    }

    public User logIn(String login, String password) throws LoginException {
        return DBHandler.getInstance().logIn(login, hash(password));
    }


    private boolean checkEmail(String email) {
        String regex ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private String hash(String input) {
        String md5Hashed = null;
        if(null == input) {
            return null;
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes(), 0, input.length());
            md5Hashed = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5Hashed;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
