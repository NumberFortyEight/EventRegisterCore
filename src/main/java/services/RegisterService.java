package services;

import entity.Event;
import entity.Period;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

public interface RegisterService {
    Integer addPeriod(Connection connection, Instant startDate, Instant endDate) throws SQLException;
    List<Period> getAllPeriods(Connection connection) throws SQLException;
    void deletePeriodAndEvents(Connection connection, Integer periodID) throws SQLException;

    Integer registerEvent(Connection connection, int locationID, int periodID) throws SQLException;
    void changeEventPeriod(Connection connection, Integer eventID, Integer periodID) throws SQLException;
    void changeEventLocation(Connection connection, Integer eventID, Integer locationID) throws SQLException;

    Integer getEventIDByLocationAndPeriodID(Connection connection, Integer locationID, Integer periodID) throws SQLException;
    List<Event> getAllEvents(Connection connection) throws SQLException;
    List<Event> getEventsByPeriod(Connection connection, Integer periodID) throws SQLException;
    Event getEventByID(Connection connection, Integer eventID) throws SQLException;

    void deleteEvent(Connection connection, Integer eventID) throws SQLException;

    @SuppressWarnings("SameReturnValue")
    boolean registerUserToEvent(Connection connection, Integer userID, Integer EventID) throws SQLException;
    void setEventActive(Connection connection, Integer eventID, boolean active) throws SQLException;
    void setUserActive(Connection connection, Integer userID, boolean active) throws SQLException;
}
