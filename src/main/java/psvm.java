import java.sql.SQLException;
import java.util.Date;

public class psvm {

    public static void main(String[] args) throws SQLException {

        EventRegisterImpl eventRegister = new EventRegisterImpl();

        //Integer eventID = eventRegister.addEventAndGetID(1111, new Date(2008), new Date(2008));
        //eventRegister.addEventAndGetID(333, 1);
        eventRegister.addUserToEvent(1337, 2);
        eventRegister.disableUser(123);

    }

}
