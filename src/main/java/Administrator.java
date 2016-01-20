import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elizaveta Kapitonova on 20.01.16.
 */
public class Administrator {
    public static final Logger LOG= Logger.getLogger(Administrator.class);

//    private String classOfComfort;
//    private int people;
//    private Date dateIn;
//    private Date dateOut;
    private List<String> res;

    public Administrator(int people, String classOfComfort, LocalDate dateIn, LocalDate dateOut) throws SQLException {
//        this.people = people;
//        this.classOfComfort = classOfComfort;
//        this.dateIn = dateIn;
//        this.dateOut = dateOut;

        ResultSet resultSet = DatabaseHandler.searchRoom(people,classOfComfort, dateIn, dateOut);
        res = new ArrayList<>();
        while(resultSet.next()) {
            res.add(resultSet.getString("name"));
        }
    }

    public List<String> getRes() {
        return res;
    }
}
