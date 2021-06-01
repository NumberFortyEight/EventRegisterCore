import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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
    private final static Logger logger = LoggerFactory.getLogger(EventRegisterImpl.class);

    private final CheckService checkService = new CheckServiceImpl();
    private final services.SQLRegisterService SQLRegisterService = new SQLRegisterServiceImpl();

    private static HikariDataSource dataSource;

    public EventRegisterImpl(String dataSourcePropertiesPath) {
        HikariConfig config = new HikariConfig(dataSourcePropertiesPath);
        dataSource = new HikariDataSource(config);
        createAllTables();
    }

    @Override
    public Integer addEvent(Integer locationID, Integer periodID) {
        try (Connection connection = dataSource.getConnection()){
            logger.info("adding event with locationID: {} and periodID: {}", locationID, periodID);

            if (checkService.isPeriodExist(connection, periodID)) {
                return SQLRegisterService.registerEvent(connection, locationID, periodID);
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
        try (Connection connection = dataSource.getConnection()){
            logger.info("deleting event: {}", eventID);
            SQLRegisterService.deleteEvent(connection, eventID);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        }
    }

    @Override
    public void changeEventPeriod(Integer eventID, Integer periodID) {
        try (Connection connection = dataSource.getConnection()){
            logger.info("update event {}, period: {}", eventID, periodID);
            if (checkService.isEventExist(connection, eventID) && checkService.isPeriodExist(connection, periodID)) {
                SQLRegisterService.changeEventPeriod(connection, eventID, periodID);
            } else {
                logger.warn("period: {} or event: {} not exist", periodID, eventID);
            }
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        }
    }

    @Override
    public Boolean isEventExist(Integer eventID) {
        try (Connection connection = dataSource.getConnection()){
            logger.info("check event: {}", eventID);
            return checkService.isEventExist(connection, eventID);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public List<Event> getAllEvents() {
        try (Connection connection = dataSource.getConnection()){
            logger.info("getting all events");
            return SQLRegisterService.getAllEvents(connection);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public Integer addPeriod(Instant startInstant, Instant endInstant) {
        try (Connection connection = dataSource.getConnection()){
            logger.info("Adding period with startInstant {} \n and endInstant {}", startInstant.toString(), endInstant.toString());
            Optional<Integer> optionalPeriod = checkService.getOptionalPeriod(connection, startInstant, endInstant);
            if (optionalPeriod.isPresent()) {
                return optionalPeriod.get();
            } else {
                Integer addedPeriodID = SQLRegisterService.addPeriod(connection, startInstant, endInstant);
                logger.info("added period have ID {}", addedPeriodID);
                return addedPeriodID;
            }
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public List<Period> getAllPeriods() {
        try (Connection connection = dataSource.getConnection()){
            logger.info("getting all periods");
            return SQLRegisterService.getAllPeriods(connection);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public Boolean isPeriodExist(Integer periodID) {
        try (Connection connection = dataSource.getConnection()){
            logger.info("check period: {}", periodID);
            return checkService.isPeriodExist(connection, periodID);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public void deletePeriodAndEvents(Integer periodID) {
        try (Connection connection = dataSource.getConnection()){
            logger.info("removing period {} and event(by period id) ", periodID);
            SQLRegisterService.deletePeriodAndEvents(connection, periodID);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        }
    }

    @Override
    public Boolean registerUserToEvent(Integer userID, Integer eventID) {
        try (Connection connection = dataSource.getConnection()){
            if (checkService.isEventExist(connection, eventID)) {
                logger.info("register user: "+ userID + " to event: " + eventID );
                return SQLRegisterService.registerUserToEvent(connection, userID, eventID);
            } else {
                logger.warn("Event not found. ID: " + eventID);
                return false;
            }
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public Boolean isUserRegistered(Integer userID, Integer eventID) {
        try (Connection connection = dataSource.getConnection()){
            if (checkService.isEventExist(connection, eventID)) {
                logger.info("Check user: "+ userID + " to event: " + eventID );
                return checkService.isUserRegistered(connection, userID, eventID);
            } else {
                logger.warn("Event not found. ID: " + eventID);
                return false;
            }
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public Boolean canUserEnter(Integer userID, Integer eventID, Instant entryTime) {
        try (Connection connection = dataSource.getConnection()){
            logger.info("checking whether user {} can log in to event {}", userID, eventID);
            return checkService.canUserEnter(connection, userID, eventID, entryTime);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public void disableEvent(Integer eventID) {
        try (Connection connection = dataSource.getConnection()){
            SQLRegisterService.setEventActive(connection, eventID, false);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        }
    }

    @Override
    public void disableUser(Integer userID) {
        try (Connection connection = dataSource.getConnection()){
            SQLRegisterService.setUserActive(connection, userID, false);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        }
    }

    @Override
    public void activateEvent(Integer eventID) {
        try (Connection connection = dataSource.getConnection()){
            SQLRegisterService.setEventActive(connection, eventID, true);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        }
    }

    @Override
    public void activateUser(Integer userID) {
        try (Connection connection = dataSource.getConnection()){
            SQLRegisterService.setUserActive(connection, userID, true);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        }
    }

    private void createAllTables() {
        try (Connection connection = dataSource.getConnection()){
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
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        }
    }
}
