package services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public interface SQLRegisterService {
    Integer addPeriodAndGetID(Connection connection, Date startDate, Date endDate) throws SQLException;
    Integer registerEventAndGetID(Connection connection, int locationID, int periodID) throws SQLException;
    boolean registerUserToEvent(Connection connection, Integer userID, Integer EventID) throws SQLException;
    void setEventActive(Connection connection, Integer eventID, boolean active) throws SQLException;
    void setUserActive(Connection connection, Integer userID, boolean active) throws SQLException;
}
