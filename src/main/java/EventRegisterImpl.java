import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

public class EventRegisterImpl implements EventRegister{

    EventCheckService eventCheckService = new EventCheckServiceImpl();
    sqlEventRegister sqlEventRegister = new SqlEventRegisterImpl();

    public static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    public static final String USER = "postgres";
    public static final String PASSWORD = "root";

    @Override
    public Integer registerEventAndGetID(int locationID, int periodID) throws SQLException {

        return null;
    }

    @Override
    public Integer registerEventAndGetID(int locationID, Date dateBegin, Date dateEnd) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        Integer existPeriodID = eventCheckService.getExistPeriodIDOrNull(connection, dateBegin, dateEnd);
        if (existPeriodID == null) {

            return sqlEventRegister.registerEventAndGetID(connection, locationID, dateBegin, dateEnd);
        } else {
            return sqlEventRegister.registerEventAndGetID(connection, locationID, existPeriodID);
        }
    }

    @Override
    public boolean registerUserToEvent(Integer userID, Integer EventID) throws SQLException {
        return false;
    }

    @Override
    public boolean isUserRegistered(Integer userID, Integer EventID) {
        return false;
    }

    @Override
    public void disableEvent(Integer eventID) {

    }

    @Override
    public void disableUser(Integer userID) {

    }
}
