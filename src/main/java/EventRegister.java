import entity.Period;

import java.sql.SQLException;
import java.util.Date;

public interface EventRegister {
    Integer registerEventAndGetID(int locationID, Date dateBegin, Date dateEnd) throws SQLException;
    Integer registerEventAndGetID(int locationID, int periodID) throws SQLException;
    boolean registerUserToEvent(Integer userID, Integer EventID) throws SQLException;
    boolean isUserRegistered(Integer userID, Integer EventID);
    void disableEvent(Integer eventID);
    void disableUser(Integer userID);
}
