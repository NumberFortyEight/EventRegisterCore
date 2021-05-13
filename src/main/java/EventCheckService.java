import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public interface EventCheckService{
    Integer getExistPeriodIDOrNull(Connection connection, Date dateBegin, Date dateEnd) throws SQLException;
    boolean isEventExist(Connection connection,int locationID, Date dateBegin, Date dateEnd);
    boolean isEventExist(Connection connection, int locationID, int periodID);
    boolean isPeriodExist(Connection connection, int periodID);

}
