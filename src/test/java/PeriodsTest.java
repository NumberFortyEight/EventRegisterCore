import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;

public class PeriodsTest {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER ="postgres";
    private static final String PASSWORD = "root";
    private final EventRegister eventRegister = new EventRegisterImpl(URL, USER, PASSWORD);

    @Test
    public void isDuplicatePeriodsHaveOneID(){
        Instant start = Instant.parse("2020-09-18T00:01:37.907Z");
        Instant end = Instant.parse("2021-10-18T00:02:06.907Z");
        Integer periodID = eventRegister.addPeriod(start, end);
        Integer periodIDTwo = eventRegister.addPeriod(start, end);
        eventRegister.deletePeriodAndEvents(periodID);
        Assert.assertEquals(periodID, periodIDTwo);
    }

    @Test
    public void eventsDeletedWithPeriod(){
        Instant start = Instant.parse("2019-09-18T00:01:37.907Z");
        Instant end = Instant.parse("2020-10-18T00:02:06.907Z");

        Integer periodID = eventRegister.addPeriod(start, end);
        Integer eventID = eventRegister.addEvent(1000, periodID);
        eventRegister.deletePeriodAndEvents(periodID);

        boolean isEventExist = eventRegister.getAllEvents()
                .stream()
                .anyMatch(event -> event.getEventID().equals(eventID) && event.getPeriodID().equals(periodID));

        boolean isPeriodExist = eventRegister.getAllPeriods()
                .stream()
                .anyMatch(period -> period.getPeriodID().equals(periodID));

        Assert.assertFalse(isEventExist);
        Assert.assertFalse(isPeriodExist);
    }

}
