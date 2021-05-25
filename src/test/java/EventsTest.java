import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;

public class EventsTest {
    public static final String URL = "jdbc:postgresql://localhost:5432/mainBase";
    public static final String USER ="postgres";
    public static final String PASSWORD = "root";
    public final EventRegister eventRegister = new EventRegisterImpl(URL, USER, PASSWORD);

    @Test
    public void addEvent(){
        Instant start = Instant.parse("2018-09-18T00:01:37.907Z");
        Instant end = Instant.parse("2021-10-18T00:02:06.907Z");

        Integer periodID = eventRegister.addPeriod(start, end);
        Integer eventID = eventRegister.addEvent(100, periodID);

        boolean isEventExist = eventRegister.getAllEvents()
                .stream()
                .anyMatch(event -> event.getEventID().equals(eventID) && event.getPeriodID().equals(periodID));

        boolean isPeriodExist = eventRegister.getAllPeriods()
                .stream()
                .anyMatch(period -> period.getPeriodID().equals(periodID));

        Assert.assertTrue(isEventExist);
        Assert.assertTrue(isPeriodExist);

        eventRegister.deletePeriodAndEvents(periodID);
    }
}
