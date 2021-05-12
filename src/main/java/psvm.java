import java.sql.SQLException;
import java.util.Date;

public class psvm {

    public static void main(String[] args) throws SQLException {
        EventRegisterImpl eventRegister = new EventRegisterImpl();
        eventRegister.registerEventAndGetID(22, new Date(), new Date());
    }

}
