
import java.sql.SQLException;
import java.util.Date;

public class psvm {

    public static void main(String[] args) throws SQLException {

        EventRegister eventRegister =
                new EventRegisterImpl("jdbc:postgresql://localhost:5432/mainBase", "postgres", "root");
        eventRegister.addUserToEvent(170, 3);
        boolean userCanEnter = eventRegister.isUserCanEnter(170, 3, new Date(2099));
        System.out.println(userCanEnter);
    }
}
