import java.sql.SQLException;
import java.time.Instant;
import java.util.Optional;

public interface EventRegister {
    /** Может возвращать null в случе sql ошибки */
    Integer addEvent(int locationID, int periodID);
    /** Может возвращать null в случе sql ошибки */
    Integer addPeriod(Instant startInstant, Instant endInstant);
    boolean registerUserToEvent(Integer userID, Integer EventID);
    boolean isUserRegistered(Integer userID, Integer EventID);
    boolean canUserEnter(Integer userID, Integer EventID, Instant entryTime);
    void disableEvent(Integer eventID);
    void disableUser(Integer userID);
    void activateEvent(Integer eventID);
    void activateUser(Integer userID);
}
