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
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        Integer eventID = sqlEventRegister.registerEventAndGetID(connection, locationID, periodID);
        connection.close();
        return eventID;
    }

    @Override
    public Integer registerEventAndGetID(int locationID, Date startDate, Date endDate) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        Integer existPeriodID = eventCheckService.getExistPeriodIDOrNull(connection, startDate, endDate);
        if (existPeriodID == null) {
            Integer periodID = sqlEventRegister.addPeriodAndGetID(connection, startDate, endDate);
            Integer eventId = sqlEventRegister.registerEventAndGetID(connection, locationID, periodID);
            connection.close();
            return eventId;
        } else {
            Integer eventID = sqlEventRegister.registerEventAndGetID(connection, locationID, existPeriodID);
            connection.close();
            return eventID;
        }
    }

    @Override
    public boolean registerUserToEvent(Integer userID, Integer eventID) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        if (eventCheckService.isEventExist(connection, eventID)) {
            boolean userIsRegistered = sqlEventRegister.registerUserToEvent(connection, userID, eventID);
            connection.close();
            return userIsRegistered;
        } else {
            // FIXME: 13.05.2021 exception
            System.out.println("event is not exist");
            connection.close();
            return false;
        }
    }

    @Override
    public boolean isUserRegistered(Integer userID, Integer EventID) {
        eventCheckService.
    }

    @Override
    public void disableEvent(Integer eventID) {

    }

    @Override
    public void disableUser(Integer userID) {

    }
}
