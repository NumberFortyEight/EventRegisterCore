import entity.Event;
import entity.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.CheckService;
import services.CheckServiceImpl;
import services.SQLRegisterServiceImpl;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class EventRegisterImpl implements EventRegister {

    private final CheckService checkService = new CheckServiceImpl();
    private final services.SQLRegisterService SQLRegisterService = new SQLRegisterServiceImpl();
    private final static Logger logger = LoggerFactory.getLogger(EventRegisterImpl.class);

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
            logger.info("adding event with locationID: {} and periodID: {}", locationID, periodID);
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            if (checkService.isPeriodExist(connection, periodID)) {
                Integer eventID = SQLRegisterService.registerEvent(connection, locationID, periodID);
                connection.close();
                return eventID;
            } else {
                logger.warn("period: {} does not exist.", periodID);
                return null;
            }
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public void deleteEvent(Integer eventID) {
        try {
            logger.info("deleting event: {}", eventID);
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            SQLRegisterService.deleteEvent(connection, eventID);
            connection.close();
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        }
    }

    @Override
    public Boolean isEventExist(Integer eventID) {
        try {
            logger.info("check event: {}", eventID);
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            Boolean isPeriodExist = checkService.isEventExist(connection, eventID);
            connection.close();
            return isPeriodExist;
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public List<Event> getAllEvents() {
        try {
            logger.info("getting all events");
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            List<Event> allEvents = SQLRegisterService.getAllEvents(connection);
            connection.close();
            return allEvents;
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public Integer addPeriod(Instant startInstant, Instant endInstant) {
        try {
            logger.info("Adding period with startInstant {} and endInstant {}", startInstant.toString(), endInstant.toString());
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            Optional<Integer> optionalPeriod = checkService.getOptionalPeriod(connection, startInstant, endInstant);
            if (optionalPeriod.isPresent()) {
                connection.close();
                return optionalPeriod.get();
            } else {
                Integer addedPeriodID = SQLRegisterService.addPeriod(connection, startInstant, endInstant);
                logger.info("added period have ID {}", addedPeriodID);
                connection.close();
                return addedPeriodID;
            }
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public List<Period> getAllPeriods() {
        try {
            logger.info("getting all periods");
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            List<Period> allPeriods = SQLRegisterService.getAllPeriods(connection);
            connection.close();
            return allPeriods;
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public Boolean isPeriodExist(Integer periodID) {
        try {
            logger.info("check period: {}", periodID);
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            Boolean isPeriodExist = checkService.isPeriodExist(connection, periodID);
            connection.close();
            return isPeriodExist;
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public void deletePeriodAndEvents(Integer periodID) {
        try {
            logger.info("removing period {} and event(by period id) ", periodID);
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            SQLRegisterService.deletePeriodAndEvents(connection, periodID);
            connection.close();
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        }
    }

    @Override
    public Boolean registerUserToEvent(Integer userID, Integer eventID) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            if (checkService.isEventExist(connection, eventID)) {
                logger.info("register user: "+ userID + " to event: " + eventID );
                boolean userIsRegistered = SQLRegisterService.registerUserToEvent(connection, userID, eventID);
                connection.close();
                return userIsRegistered;
            } else {
                logger.warn("Event not found. ID: " + eventID);
                connection.close();
                return false;
            }
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public Boolean isUserRegistered(Integer userID, Integer eventID) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            if (checkService.isEventExist(connection, eventID)) {
                logger.info("Check user: "+ userID + " to event: " + eventID );
                boolean userRegistered = checkService.isUserRegistered(connection, userID, eventID);
                connection.close();
                return userRegistered;
            } else {
                logger.warn("Event not found. ID: " + eventID);
                connection.close();
                return false;
            }
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public Boolean canUserEnter(Integer userID, Integer eventID, Instant entryTime) {
        try {
            logger.info("checking whether user {} can log in to event {}", userID, eventID);
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            boolean isUserCanEnter = checkService.canUserEnter(connection, userID, eventID, entryTime);
            connection.close();
            return isUserCanEnter;
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public void disableEvent(Integer eventID) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            SQLRegisterService.setEventActive(connection, eventID, false);
            connection.close();
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        }
    }

    @Override
    public void disableUser(Integer userID) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            SQLRegisterService.setUserActive(connection, userID, false);
            connection.close();
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        }
    }

    @Override
    public void activateEvent(Integer eventID) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            SQLRegisterService.setEventActive(connection, eventID, true);
            connection.close();
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        }
    }

    @Override
    public void activateUser(Integer userID) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            SQLRegisterService.setUserActive(connection, userID, true);
            connection.close();
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        }
    }

    private void createAllTables() {
        try {
            Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance();
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            connection.prepareStatement("create table if not exists events\n" +
                    "(\n" +
                    "    event_id    serial  not null\n" +
                    "        constraint event_pk\n" +
                    "            primary key,\n" +
                    "    location_id integer not null,\n" +
                    "    period_id   integer not null\n" +
                    ");\n" +
                    "\n" +
                    "alter table events\n" +
                    "    owner to postgres;").execute();
            connection.prepareStatement("create table if not exists passlist\n" +
                    "(\n" +
                    "    user_id         integer not null,\n" +
                    "    event_id        integer not null,\n" +
                    "    is_user_active  varchar,\n" +
                    "    is_event_active varchar\n" +
                    ");\n" +
                    "\n" +
                    "alter table passlist\n" +
                    "    owner to postgres;").execute();
            connection.prepareStatement("create table if not exists periods\n" +
                    "(\n" +
                    "    period_id  serial                   not null\n" +
                    "        constraint periods_pk\n" +
                    "            primary key,\n" +
                    "    start_date timestamp with time zone not null,\n" +
                    "    end_date   timestamp with time zone not null\n" +
                    ");\n" +
                    "\n" +
                    "alter table periods\n" +
                    "    owner to postgres;").execute();
            connection.close();
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException classNotFoundException) {
            logger.error("Driver postgres exception ", classNotFoundException);
        }
    }
}
