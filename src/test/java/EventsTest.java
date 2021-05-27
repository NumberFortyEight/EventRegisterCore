import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;

public class EventsTest {
    private static final String URL = "jdbc:postgresql://localhost:5432/mainBase";
    private static final String USER ="postgres";
    private static final String PASSWORD = "root";
    private final EventRegister eventRegister = new EventRegisterImpl(URL, USER, PASSWORD);

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
    @Test
    public void deleteEvent(){
        Instant start = Instant.parse("2018-09-18T00:01:37.907Z");
        Instant end = Instant.parse("2021-10-18T00:02:06.907Z");

        Integer periodID = eventRegister.addPeriod(start, end);
        Integer eventID = eventRegister.addEvent(100, periodID);

        eventRegister.deleteEvent(eventID);
        Assert.assertFalse(eventRegister.getAllEvents()
                .stream()
                .anyMatch(event -> event.getEventID().equals(eventID)));
    }
}
