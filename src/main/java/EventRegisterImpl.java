import services.CheckService;
import services.CheckServiceImpl;
import services.SQLRegisterServiceImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

public class EventRegisterImpl implements EventRegister{

    private final CheckService checkService = new CheckServiceImpl();
    private final services.SQLRegisterService SQLRegisterService = new SQLRegisterServiceImpl();

    public static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    public static final String USER = "postgres";
    public static final String PASSWORD = "root";

    @Override
    public Integer addEventAndGetID(int locationID, int periodID) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        Integer eventID = SQLRegisterService.registerEventAndGetID(connection, locationID, periodID);
        connection.close();
        return eventID;
    }

    @Override
    public Integer addEventAndGetID(int locationID, Date startDate, Date endDate) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        Integer existPeriodID = checkService.getExistPeriodIDOrNull(connection, startDate, endDate);
        if (existPeriodID == null) {
            Integer periodID = SQLRegisterService.addPeriodAndGetID(connection, startDate, endDate);
            Integer eventId = SQLRegisterService.registerEventAndGetID(connection, locationID, periodID);
            connection.close();
            return eventId;
        } else {
            Integer eventID = SQLRegisterService.registerEventAndGetID(connection, locationID, existPeriodID);
            connection.close();
            return eventID;
        }
    }

    @Override
    public boolean addUserToEvent(Integer userID, Integer eventID) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        if (checkService.isEventExist(connection, eventID)) {
            boolean userIsRegistered = SQLRegisterService.registerUserToEvent(connection, userID, eventID);
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
    public boolean isUserRegistered(Integer userID, Integer eventID) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        if (checkService.isEventExist(connection, eventID)) {
            boolean userRegistered = checkService.isUserRegistered(connection, userID, eventID);
            connection.close();
            return userRegistered;
        } else {
            // FIXME: 13.05.2021 exception
            System.out.println("event is not exist");
            connection.close();
            return false;
        }
    }

    @Override
    public void disableEvent(Integer eventID) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        SQLRegisterService.disableEvent(connection, eventID);
        connection.close();
    }

    @Override
    public void disableUser(Integer userID) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        SQLRegisterService.disableUser(connection, userID);
        connection.close();
    }

    @Override
    public void activateEvent(Integer eventID) throws SQLException {

    }

    @Override
    public void activateUser(Integer userID) throws SQLException {

    }
}
