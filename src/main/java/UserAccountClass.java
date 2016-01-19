/**
 * Created by Elizaveta Kapitonova on 13.01.16.
 */

import org.apache.log4j.Logger;

import javax.security.auth.login.LoginException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAccountClass {
    public static final Logger LOG= Logger.getLogger(UserAccountClass.class);

    private final String login;


    // log in
    public UserAccountClass(String login, String password)
            throws LoginException{
        this.login = login;

        boolean completed = DatabaseHandler.logIn(this.login, hash(password));
        if (!completed) {
            throw new LoginException("Wrong login or password");
        }
    }

    // 3 arguments mean that it is registration
    public UserAccountClass(String login, String password, String email)
            throws LoginException{
        this.login = login;
        if (checkEmail(email)) {
            boolean completed = DatabaseHandler.register(this.login, hash(password), email);
            LOG.debug("email is valid");
            if (!completed) {
                LOG.debug("email is already registered");
                throw new LoginException("Such login/email is already registered");

            }
        }
        else {
            LOG.debug("email is not valid");
            throw new LoginException("Wrong email");
        }
    }

    private boolean checkEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
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

}
