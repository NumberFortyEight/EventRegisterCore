import java.sql.*;
import java.util.Date;

public class EventCheckServiceImpl implements EventCheckService {

    @Override
    public Integer getExistPeriodIDOrNull(Connection connection, Date dateBegin, Date dateEnd) throws SQLException {
        String sql = "SELECT * FROM periods WHERE start_date = ? AND end_date = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setTimestamp(1, new Timestamp(dateBegin.getTime()));
        preparedStatement.setTimestamp(2, new Timestamp(dateEnd.getTime()));
        preparedStatement.executeQuery();
        ResultSet resultSet = preparedStatement.getResultSet();
        if (resultSet == null) {
            return null;
        }
        resultSet.next();
        return resultSet.getInt(1);
    }

    @Override
    public boolean isEventExist(Connection connection, Integer eventID) throws SQLException {
        String sql = "SELECT * FROM events WHERE event_id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, eventID);
        preparedStatement.executeQuery();
        return !(preparedStatement.getResultSet() == null);
    }

    @Override
    public boolean isUserRegistered(Connection connection, Integer userID, Integer EventID) {
        String sql = "SELECT * FROM events WHERE event_id = ?;";
    }
}
