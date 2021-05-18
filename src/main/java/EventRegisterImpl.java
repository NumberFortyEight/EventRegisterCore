import services.CheckService;
import services.CheckServiceImpl;
import services.SQLRegisterServiceImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

public class EventRegisterImpl implements EventRegister {

    private final CheckService checkService = new CheckServiceImpl();
    private final services.SQLRegisterService SQLRegisterService = new SQLRegisterServiceImpl();

    private final String DB_URL;
    private final String USER;
    private final String PASSWORD;

    public EventRegisterImpl(String DB_URL, String USER, String PASSWORD) {
        this.DB_URL = DB_URL;
        this.USER = USER;
        this.PASSWORD = PASSWORD;
        createAllTables();
    }

    @Override
    public Integer addEvent(int locationID, int periodID) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            Integer eventID = SQLRegisterService.registerEvent(connection, locationID, periodID);
            connection.close();
            return eventID;
        } catch (SQLException sqlException) {
            return null;
        }
    }

    @Override
    public Integer addPeriod(Instant startInstant, Instant endInstant) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            Optional<Integer> optionalPeriod = checkService.getOptionalPeriod(connection, startInstant, endInstant);
            if (optionalPeriod.isPresent()) {
                connection.close();
                return optionalPeriod.get();
            } else {
                Integer addPeriod = SQLRegisterService.addPeriod(connection, startInstant, endInstant);
                connection.close();
                return addPeriod;
            }
        } catch (SQLException sqlException) {
            return null;
        }
    }

    @Override
    public boolean registerUserToEvent(Integer userID, Integer eventID) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            if (checkService.isEventExist(connection, eventID)) {
                boolean userIsRegistered = SQLRegisterService.registerUserToEvent(connection, userID, eventID);
                connection.close();
                return userIsRegistered;
            } else {
                connection.close();
                return false;
            }
        } catch (SQLException sqlException) {
            return false;
        }
    }

    @Override
    public boolean isUserRegistered(Integer userID, Integer eventID) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            if (checkService.isEventExist(connection, eventID)) {
                boolean userRegistered = checkService.isUserRegistered(connection, userID, eventID);
                connection.close();
                return userRegistered;
            } else {
                connection.close();
                return false;
            }
        } catch (SQLException sqlException) {
            return false;
        }
    }

    @Override
    public boolean canUserEnter(Integer userID, Integer eventID, Instant entryTime) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            boolean isUserCanEnter = checkService.canUserEnter(connection, userID, eventID, entryTime);
            connection.close();
            return isUserCanEnter;
        } catch (SQLException sqlException) {
            return false;
        }
    }

    @Override
    public void disableEvent(Integer eventID) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            SQLRegisterService.setEventActive(connection, eventID, false);
            connection.close();
        } catch (SQLException ignored) {
        }
    }

    @Override
    public void disableUser(Integer userID) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            SQLRegisterService.setEventActive(connection, userID, false);
            connection.close();
        } catch (SQLException ignored) {
        }
    }

    @Override
    public void activateEvent(Integer eventID) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            SQLRegisterService.setEventActive(connection, eventID, true);
            connection.close();
        } catch (SQLException ignored) {
        }
    }

    @Override
    public void activateUser(Integer userID){
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            SQLRegisterService.setEventActive(connection, userID, true);
            connection.close();
        } catch (SQLException ignored) {
        }
    }

    private void createAllTables() {
        try {
            Class.forName("org.postgresql.Driver");

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
        } catch (ClassNotFoundException | SQLException ignored) {
        }
    }
}
