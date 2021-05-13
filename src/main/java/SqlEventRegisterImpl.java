import entity.Period;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SqlEventRegisterImpl implements sqlEventRegister {

    public Integer addPeriodAndGetID(Connection connection, Date dateBegin, Date dateEnd) throws SQLException {
        String sql = "INSERT INTO periods (start_date, end_date) Values (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setTimestamp(1, new Timestamp(dateBegin.getTime()));
        preparedStatement.setTimestamp(2, new Timestamp(dateEnd.getTime()));
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        connection.close();
        generatedKeys.next();
        return generatedKeys.getInt(1);
    }

    @Override
    public Integer createPeriodAndGetID(Connection connection, Date startDate, Date endDate) {
        return null;
    }

    @Override
    public Integer registerEventAndGetID(Connection connection, int locationID, int periodID) throws SQLException {
        String sql = "INSERT INTO events (location_id, period_id) Values (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, locationID);
        preparedStatement.setInt(2, periodID);
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        connection.close();
        generatedKeys.next();
        return generatedKeys.getInt(1);
    }

    public List<Period> getAllPeriods(Connection connection) throws SQLException {
        String sql = "SELECT * FROM periods;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Period> periodList = new ArrayList<>();
        while (resultSet.next()) {
            Timestamp start_date = resultSet.getTimestamp("start_date");
            Timestamp end_date = resultSet.getTimestamp("end_date");
            Period period = new Period(resultSet.getInt("id"), new Date(start_date.getTime()), new Date(end_date.getTime()));
            periodList.add(period);
        }
        connection.close();
        return periodList;
    }

    @Override
    public boolean registerUserToEvent(Connection connection, Integer userID, Integer eventID) throws SQLException {
        String sql = "INSERT INTO passlist (user_id, event_id) VALUES (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, userID);
        preparedStatement.setInt(2, eventID);
        preparedStatement.execute();
        return true;
    }

    @Override
    public boolean isUserRegistered(Connection connection, Integer userID, Integer EventID) {
        return false;
    }

    @Override
    public void disableEvent(Connection connection, Integer eventID) {

    }

    @Override
    public void disableUser(Connection connection, Integer userID) {

    }
}
