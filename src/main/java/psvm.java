import java.sql.SQLException;
import java.util.Date;

public class psvm {

    public static void main(String[] args) throws SQLException {

        EventRegister eventRegister =
                new EventRegisterImpl("jdbc:postgresql://localhost:5432/postgres", "postgres", "root");
        eventRegister.addUserToEvent(1337, 2);
        eventRegister.disableUser(123);

    }

}
