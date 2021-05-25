import entity.Event;
import entity.Period;

import java.time.Instant;
import java.util.List;

public interface EventRegister {
    /** Может возвращать null в случе sql ошибки */
    Integer addEvent(int locationID, int periodID);
    List<Event> getAllEvents();
    /** Может возвращать null в случе sql ошибки */
    Integer addPeriod(Instant startInstant, Instant endInstant);
    List<Period> getAllPeriods();
    Boolean isPeriodExist(Integer periodID);
    void deletePeriodAndEvents(Integer periodID);
    Boolean registerUserToEvent(Integer userID, Integer EventID);
    Boolean isUserRegistered(Integer userID, Integer EventID);
    Boolean canUserEnter(Integer userID, Integer EventID, Instant entryTime);
    void disableEvent(Integer eventID);
    void disableUser(Integer userID);
    void activateEvent(Integer eventID);
    void activateUser(Integer userID);
}
