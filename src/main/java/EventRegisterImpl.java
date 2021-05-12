import entity.Period;

import java.sql.*;
import java.util.Date;

public class EventRegisterImpl implements EventRegister {

    public static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    public static final String USER = "postgres";
    public static final String PASSWORD = "root";

    public void createTables() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    public Integer addPeriodAndGetID(Date dateBegin, Date dateEnd) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        String sql = "INSERT INTO periods (start_date, end_date) Values (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setDate(1, new java.sql.Date(dateBegin.getTime()));
        preparedStatement.setDate(2, new java.sql.Date(dateEnd.getTime()));
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        connection.close();
        generatedKeys.next();
        return generatedKeys.getInt(1);
    }

    @Override
    public Integer registerEventAndGetID(int locationID, Date dateBegin, Date dateEnd) throws SQLException {
        Integer periodID = addPeriodAndGetID(dateBegin, dateEnd);
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
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

    @Override
    public Integer registerEventAndGetID(int locationID, int periodID) {
        return null;
    }

    @Override
    public boolean registerUserToEvent(Integer userID, Integer EventID) {
        return false;
    }

    @Override
    public boolean isUserRegistered(Integer userID, Integer EventID) {
        return false;
    }
}
