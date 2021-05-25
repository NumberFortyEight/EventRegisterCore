
import java.time.Instant;

public class psvm {

    public static void main(String[] args){

       // EventRegister eventRegister =
       //         new EventRegisterImpl("jdbc:postgresql://localhost:5432/mainBase", "postgres", "root");
        Instant instant = Instant.parse("2005-09-18T00:01:37.907Z");
        Instant instantTwo = Instant.parse("2004-10-18T00:02:06.907Z");

        //eventRegister.addEvent(100, eventRegister.addPeriod(instant, instantTwo));
        //eventRegister.registerUserToEvent(5, 1);
        //eventRegister.activateUser(5);
    }
}
