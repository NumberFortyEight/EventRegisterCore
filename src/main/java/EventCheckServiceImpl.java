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
    public boolean isEventExist(Connection connection, int locationID, Date dateBegin, Date dateEnd) {
        return false;
    }

    @Override
    public boolean isEventExist(Connection connection, int locationID, int periodID) {
        return false;
    }

    @Override
    public boolean isPeriodExist(Connection connection, int periodID) {
        return false;
    }
}
