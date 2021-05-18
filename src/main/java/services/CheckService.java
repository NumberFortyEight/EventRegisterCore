package services;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

public interface CheckService {
    Optional<Integer> getOptionalPeriod(Connection connection, Instant dateBegin, Instant dateEnd) throws SQLException;
    boolean isEventExist(Connection connection,Integer eventID) throws SQLException;
    boolean isUserRegistered(Connection connection, Integer userID, Integer eventID) throws SQLException;
    boolean canUserEnter(Connection connection, Integer userID, Integer eventID, Instant instant) throws SQLException;
}
