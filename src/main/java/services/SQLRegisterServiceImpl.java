package services;

import java.sql.*;
import java.util.Date;

public class SQLRegisterServiceImpl implements SQLRegisterService {

    public Integer addPeriodAndGetID(Connection connection, Date startDate, Date endDate) throws SQLException {
        String sql = "INSERT INTO periods (start_date, end_date) Values (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setTimestamp(1, new Timestamp(startDate.getTime()));
        preparedStatement.setTimestamp(2, new Timestamp(endDate.getTime()));
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        return generatedKeys.getInt(1);
    }

    @Override
    public Integer registerEventAndGetID(Connection connection, int locationID, int periodID) throws SQLException {
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
    public boolean registerUserToEvent(Connection connection, Integer userID, Integer eventID) throws SQLException {
        String sql = "INSERT INTO passlist (user_id, event_id, is_user_active, is_event_active ) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userID);
        preparedStatement.setInt(2, eventID);
        preparedStatement.setBoolean(3, true);
        preparedStatement.setBoolean(4, true);
        preparedStatement.execute();
        return true;
    }

    @Override
    public void disableEvent(Connection connection, Integer eventID) throws SQLException {
        String sql = "UPDATE passlist SET is_event_active = false WHERE event_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, eventID);
        preparedStatement.execute();
    }

    @Override
    public void disableUser(Connection connection, Integer userID) throws SQLException {
        String sql = "UPDATE passlist SET is_user_active = false WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userID);
        preparedStatement.execute();
    }
}
