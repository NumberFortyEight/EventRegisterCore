package services;

import entity.Event;
import entity.Period;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQLRegisterServiceImpl implements SQLRegisterService {

    public Integer addPeriod(Connection connection, Instant startDate, Instant endDate) throws SQLException {
        String sql = "INSERT INTO periods (start_date, end_date) Values (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setTimestamp(1, Timestamp.from(startDate));
        preparedStatement.setTimestamp(2, Timestamp.from(endDate));
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        return generatedKeys.getInt(1);
    }

    @Override
    public List<Period> getAllPeriods(Connection connection) throws SQLException {
        List<Period> periodList = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM periods");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int period_id = resultSet.getInt("period_id");
            Timestamp start_date = resultSet.getTimestamp("start_date");
            Timestamp end_date = resultSet.getTimestamp("end_date");
            periodList.add(new Period(period_id, start_date.toInstant(), end_date.toInstant()));
        }
        return periodList;
    }

    @Override
    public void deletePeriodAndEvents(Connection connection, Integer periodID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM periods WHERE period_id = ?");
        preparedStatement.setInt(1, periodID);
        preparedStatement.execute();
        PreparedStatement preparedStatementForEvent = connection.prepareStatement("DELETE FROM events WHERE period_id = ?");
        preparedStatementForEvent.setInt(1, periodID);
        preparedStatementForEvent.execute();
    }

    @Override
    public Integer registerEvent(Connection connection, int locationID, int periodID) throws SQLException {
        String sql = "INSERT INTO events (location_id, period_id) Values (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, locationID);
        preparedStatement.setInt(2, periodID);
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        return generatedKeys.getInt(1);
    }

    @Override
    public List<Event> getAllEvents(Connection connection) throws SQLException {
        List<Event> eventList = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM events");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int event_id = resultSet.getInt("event_id");
            int location_id = resultSet.getInt("location_id");
            int period_id = resultSet.getInt("period_id");
            eventList.add(new Event(event_id, location_id, period_id));
        }
        return eventList;
    }

    @Override
    public void deleteEvent(Connection connection, Integer eventID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM events WHERE event_id = ?");
        preparedStatement.setInt(1, eventID);
        preparedStatement.execute();
        PreparedStatement preparedStatementForPassList = connection.prepareStatement("DELETE FROM passlist WHERE event_id = ?");
        preparedStatementForPassList.setInt(1, eventID);
        preparedStatementForPassList.execute();
    }

    @Override
    public boolean registerUserToEvent(Connection connection, Integer userID, Integer eventID) throws SQLException {
        String sql = "INSERT INTO passlist (user_id, event_id, is_user_active, is_event_active ) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userID);
        preparedStatement.setInt(2, eventID);
        preparedStatement.setString(3, "true");
        preparedStatement.setString(4, "true");
        preparedStatement.execute();
        return true;
    }

    @Override
    public void setEventActive(Connection connection, Integer eventID, boolean active) throws SQLException {
        String sql = "UPDATE passlist SET is_event_active = ? WHERE event_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setBoolean(1, active);
        preparedStatement.setInt(2, eventID);
        preparedStatement.executeUpdate();
    }

    @Override
    public void setUserActive(Connection connection, Integer userID, boolean active) throws SQLException {
        String sql = "UPDATE passlist SET is_user_active = ? WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setBoolean(1, active);
        preparedStatement.setInt(2, userID);
        preparedStatement.executeUpdate();
    }
}
