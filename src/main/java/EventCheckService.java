import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public interface EventCheckService{
    Integer getExistPeriodIDOrNull(Connection connection, Date dateBegin, Date dateEnd) throws SQLException;
    boolean isEventExist(Connection connection,Integer eventID) throws SQLException;
    boolean isUserRegistered(Connection connection, Integer userID, Integer EventID);
}
