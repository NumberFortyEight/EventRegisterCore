package services;

import java.sql.*;
import java.time.Instant;
import java.util.Optional;

public class CheckServiceImpl implements CheckService {

    @Override
    public Optional<Integer> getOptionalPeriod(Connection connection, Instant dateBegin, Instant dateEnd) throws SQLException {
        String sql = "SELECT * FROM periods WHERE start_date = ? AND end_date = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setTimestamp(1, Timestamp.from(dateBegin));
        preparedStatement.setTimestamp(2, Timestamp.from(dateEnd));
        preparedStatement.executeQuery();
        ResultSet resultSet = preparedStatement.getResultSet();
        while (resultSet.next()) {
            return Optional.of(resultSet.getInt("period_id"));
        }
        return Optional.empty();
    }

    @Override
    public boolean isEventExist(Connection connection, Integer eventID) {
        try {
            String sql = "SELECT * FROM events WHERE event_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, eventID);
            preparedStatement.executeQuery();
            return preparedStatement.getResultSet().next();
        } catch (SQLException sqlException) {
            return false;
        }
    }

    @Override
    public boolean isUserRegistered(Connection connection, Integer userID, Integer eventID) throws SQLException {
        String sql = "SELECT * FROM passlist WHERE user_id = ? AND event_id = ? AND is_user_active = true AND is_event_active = true;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userID);
        preparedStatement.setInt(2, eventID);
        preparedStatement.executeQuery();
        return preparedStatement.getResultSet().next();
    }

    public boolean canUserEnter(Connection connection, Integer userID, Integer eventID, Instant instant) throws SQLException {
        if (isUserRegistered(connection, userID, eventID)) {
            String sql = "SELECT start_date, end_date FROM events INNER JOIN periods ON events.period_id = periods.period_id WHERE event_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, eventID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Timestamp start_date = resultSet.getTimestamp("start_date");
                Timestamp current = Timestamp.from(instant);
                Timestamp end_date = resultSet.getTimestamp("end_date");
                return current.after(start_date) && current.before(end_date);
            } else return false;
        } else {
            return false;
        }
    }
}
