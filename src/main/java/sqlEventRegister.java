import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public interface sqlEventRegister {
    Integer addPeriodAndGetID(Connection connection, Date startDate, Date endDate) throws SQLException;
    Integer registerEventAndGetID(Connection connection, int locationID, int periodID) throws SQLException;
    boolean registerUserToEvent(Connection connection, Integer userID, Integer EventID) throws SQLException;
    boolean isUserRegistered(Connection connection, Integer userID, Integer EventID);
    void disableEvent(Connection connection, Integer eventID);
    void disableUser(Connection connection, Integer userID);
}
