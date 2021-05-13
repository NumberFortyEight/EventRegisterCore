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

    private final String DB_URL;
    private final String USER;
    private final String PASSWORD;

    public EventRegisterImpl(String DB_URL, String USER, String PASSWORD) throws SQLException {
        this.DB_URL = DB_URL;
        this.USER = USER;
        this.PASSWORD = PASSWORD;
        createAllTables();
    }

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
            connection.close();
            return false;
        }
    }

    @Override
    public boolean isUserCanEnter(Integer userID, Integer eventID, Date entryTime) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        boolean isUserCanEnter = checkService.isUserCanEnter(connection, userID, eventID, entryTime);
        connection.close();
        return isUserCanEnter;
    }

    @Override
    public void disableEvent(Integer eventID) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        SQLRegisterService.setEventActive(connection, eventID, false);
        connection.close();
    }

    @Override
    public void disableUser(Integer userID) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        SQLRegisterService.setEventActive(connection, userID, false);
        connection.close();
    }

    @Override
    public void activateEvent(Integer eventID) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        SQLRegisterService.setEventActive(connection, eventID, true);
        connection.close();
    }

    @Override
    public void activateUser(Integer userID) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        SQLRegisterService.setEventActive(connection, userID, true);
        connection.close();
    }

    private void createAllTables() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        connection.prepareStatement("create table if not exists events (" +
                "    event_id    serial  not null " +
                "        constraint event_pk " +
                "            primary key, " +
                "    location_id integer not null, " +
                "    period_id   integer not null );" +
                "alter table events " +
                "    owner to postgres; ").execute();
        connection.prepareStatement("create table if not exists passlist (" +
                "    user_id         integer not null, " +
                "    event_id        integer not null, " +
                "    is_user_active  boolean, " +
                "    is_event_active boolean ); " +
                "alter table passlist " +
                "    owner to postgres; ").execute();
        connection.prepareStatement("create table if not exists periods (" +
                "    period_id  serial    not null, " +
                "    start_date timestamp not null, " +
                "    end_date   timestamp not null );" +
                "alter table periods " +
                "    owner to postgres;").execute();
        connection.close();
    }
}
