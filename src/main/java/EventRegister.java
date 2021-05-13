import entity.Period;

import java.sql.SQLException;
import java.util.Date;

public interface EventRegister {
    Integer addEventAndGetID(int locationID, Date dateBegin, Date dateEnd) throws SQLException;
    Integer addEventAndGetID(int locationID, int periodID) throws SQLException;
    boolean addUserToEvent(Integer userID, Integer EventID) throws SQLException;
    boolean isUserRegistered(Integer userID, Integer EventID) throws SQLException;
    void disableEvent(Integer eventID) throws SQLException;
    void disableUser(Integer userID) throws SQLException;
    void activateEvent(Integer eventID) throws SQLException;
    void activateUser(Integer userID) throws SQLException;
}
