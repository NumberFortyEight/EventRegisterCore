import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import entity.Event;
import entity.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.CheckService;
import services.CheckServiceImpl;
import services.RegisterService;
import services.RegisterServiceImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class EventRegisterImpl implements EventRegister {
    private final static Logger logger = LoggerFactory.getLogger(EventRegisterImpl.class);

    private final CheckService checkService = new CheckServiceImpl();
    private final RegisterService registerService = new RegisterServiceImpl();
    private static HikariDataSource dataSource;

    public EventRegisterImpl(String dataSourcePropertiesPath) {
        HikariConfig config = new HikariConfig(dataSourcePropertiesPath);
        dataSource = new HikariDataSource(config);
        createAllTables();
    }

    @Override
    public Integer addEvent(Integer locationID, Integer periodID) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("adding event with locationID: {} and periodID: {}", locationID, periodID);
            if (checkService.isPeriodExist(connection, periodID)) {
                Integer duplicateEventID = registerService.getEventIDByLocationAndPeriodID(connection, locationID, periodID);
                if (duplicateEventID == null) {
                    return registerService.registerEvent(connection, locationID, periodID);
                } else {
                    logger.warn("event with periodID {} and locationID {} is a duplicate of event {}",
                            periodID, locationID, duplicateEventID);
                    return duplicateEventID;
                }

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
        try (Connection connection = dataSource.getConnection()) {
            logger.info("deleting event: {}", eventID);
            registerService.deleteEvent(connection, eventID);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        }
    }

    @Override
    public void changeEventPeriod(Integer eventID, Integer periodID) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("update event {}, period: {}", eventID, periodID);
            if (checkService.isEventExist(connection, eventID) && checkService.isPeriodExist(connection, periodID)) {
                registerService.changeEventPeriod(connection, eventID, periodID);
            } else {
                logger.warn("period: {} or event: {} not exist", periodID, eventID);
            }
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        }
    }

    @Override
    public void changeEventLocation(Integer eventID, Integer locationID) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("change event: {} location to: {}", eventID, locationID);
            registerService.changeEventLocation(connection, eventID, locationID);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        }
    }

    @Override
    public Boolean isEventExist(Integer eventID) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("check event: {}", eventID);
            return checkService.isEventExist(connection, eventID);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public Event getEventByID(Integer eventID) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("getting Event by id: {}", eventID);
            return registerService.getEventByID(connection, eventID);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public List<Event> getEventsByPeriod(Integer periodID) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("getting Events by period: {}", periodID);
            return registerService.getEventsByPeriod(connection, periodID);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public List<Event> getAllEvents() {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("getting all events");
            return registerService.getAllEvents(connection);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public Integer addPeriod(Instant startInstant, Instant endInstant) {
        if (startInstant.isAfter(endInstant)) {
            logger.warn("startDate after endDate");
            return null;
        }

        try (Connection connection = dataSource.getConnection()) {
            Optional<Integer> optionalPeriod = checkService.getOptionalPeriod(connection, startInstant, endInstant);
            if (optionalPeriod.isPresent()) {
                Integer duplicatePeriodID = optionalPeriod.get();
                logger.warn("duplicate period: ID {}", duplicatePeriodID);
                return duplicatePeriodID;
            } else {
                logger.info("Adding period with startInstant {} and endInstant {}", startInstant.toString(), endInstant.toString());
                Integer addedPeriodID = registerService.addPeriod(connection, startInstant, endInstant);
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
        try (Connection connection = dataSource.getConnection()) {
            logger.info("getting all periods");
            return registerService.getAllPeriods(connection);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public Boolean isPeriodExist(Integer periodID) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("check period: {}", periodID);
            return checkService.isPeriodExist(connection, periodID);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public void deletePeriodAndEvents(Integer periodID) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("removing period {} and event(by period id) ", periodID);
            registerService.deletePeriodAndEvents(connection, periodID);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        }
    }

    @Override
    public Boolean registerUserToEvent(Integer userID, Integer eventID) {
        try (Connection connection = dataSource.getConnection()) {
            if (checkService.isEventExist(connection, eventID)) {
                logger.info("register user: " + userID + " to event: " + eventID);
                return registerService.registerUserToEvent(connection, userID, eventID);
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
        try (Connection connection = dataSource.getConnection()) {
            if (checkService.isEventExist(connection, eventID)) {
                logger.info("Check user: " + userID + " to event: " + eventID);
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
        try (Connection connection = dataSource.getConnection()) {
            logger.info("checking whether user {} can log in to event {}", userID, eventID);
            return checkService.canUserEnter(connection, userID, eventID, entryTime);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
            return null;
        }
    }

    @Override
    public void setEventActivity(Integer eventID, Boolean activity) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("set event: {} activity: {}", eventID, activity.toString());
            registerService.setEventActive(connection, eventID, activity);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        }
    }

    @Override
    public void setUserActivity(Integer userID, Boolean activity) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("set user: {} activity: {}", userID, activity.toString());
            registerService.setUserActive(connection, userID, activity);
        } catch (SQLException sqlException) {
            logger.error("Connection error ", sqlException);
        }
    }

    private void createAllTables() {
        try (Connection connection = dataSource.getConnection()) {
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
