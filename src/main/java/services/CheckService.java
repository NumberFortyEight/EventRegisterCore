package services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public interface CheckService {
    Integer getExistPeriodIDOrNull(Connection connection, Date dateBegin, Date dateEnd) throws SQLException;
    boolean isEventExist(Connection connection,Integer eventID) throws SQLException;
    boolean isUserRegistered(Connection connection, Integer userID, Integer eventID) throws SQLException;
    boolean isUserCanEnter(Connection connection, Integer userID, Integer eventID, Date entryTime) throws SQLException;
}
