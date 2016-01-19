import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * Created by Elizaveta Kapitonova on 20.01.16.
 */
public class RoomSearcher {
    public static final Logger LOG= Logger.getLogger(RoomSearcher.class);

//    private String classOfComfort;
//    private int people;
//    private Date dateIn;
//    private Date dateOut;
    private List<String> res;

    public RoomSearcher(int people, String classOfComfort, Date dateIn, Date dateOut) {
//        this.people = people;
//        this.classOfComfort = classOfComfort;
//        this.dateIn = dateIn;
//        this.dateOut = dateOut;
        res = DatabaseHandler.seachRoom(people,classOfComfort, dateIn, dateOut);
    }

    public List<String> getRes() {
        return res;
    }
}
