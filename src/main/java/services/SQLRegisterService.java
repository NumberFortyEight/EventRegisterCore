package services;

import entity.Event;
import entity.Period;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public interface SQLRegisterService {
    Integer addPeriod(Connection connection, Instant startDate, Instant endDate) throws SQLException;
    List<Period> getAllPeriods(Connection connection) throws SQLException;
    void deletePeriodAndEvents(Connection connection, Integer periodID) throws SQLException;
    Integer registerEvent(Connection connection, int locationID, int periodID) throws SQLException;
    List<Event> getAllEvents(Connection connection) throws SQLException;
    void deleteEvent(Connection connection, Integer eventID);
    boolean registerUserToEvent(Connection connection, Integer userID, Integer EventID) throws SQLException;
    void setEventActive(Connection connection, Integer eventID, boolean active) throws SQLException;
    void setUserActive(Connection connection, Integer userID, boolean active) throws SQLException;
}
