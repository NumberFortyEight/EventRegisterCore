import java.sql.SQLException;
import java.util.Date;

public class psvm {

    public static void main(String[] args) throws SQLException {
        EventRegisterImpl eventRegister = new EventRegisterImpl();
        Integer eventID = eventRegister.addEventAndGetID(2222, new Date(2090), new Date(2090));
        eventRegister.a


    }

}
