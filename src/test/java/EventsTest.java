import entity.Event;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.List;

public class EventsTest {
    private final EventRegister eventRegister = new EventRegisterImpl("datasource.properties");

    @Test
    public void addEvent() {
        Instant start = Instant.parse("2018-09-18T00:01:37.907Z");
        Instant end = Instant.parse("2021-10-18T00:02:06.907Z");

        Integer periodID = eventRegister.addPeriod(start, end);
        Integer eventID = eventRegister.addEvent(100, periodID);

        boolean isEventExist = eventRegister.isEventExist(eventID) &&
                eventRegister.getAllEvents()
                        .stream()
                        .anyMatch(event -> event.getEventID().equals(eventID) && event.getPeriodID().equals(periodID));

        boolean isPeriodExist = eventRegister.isPeriodExist(periodID) &&
                eventRegister.getAllPeriods()
                        .stream()
                        .anyMatch(period -> period.getPeriodID().equals(periodID));

        Assert.assertTrue(isEventExist);
        Assert.assertTrue(isPeriodExist);

        eventRegister.deletePeriodAndEvents(periodID);
    }

    @Test
    public void deleteEvent() {
        Instant start = Instant.parse("2018-09-18T00:01:37.907Z");
        Instant end = Instant.parse("2021-10-18T00:02:06.907Z");

        Integer periodID = eventRegister.addPeriod(start, end);
        Integer eventID = eventRegister.addEvent(100, periodID);

        eventRegister.deleteEvent(eventID);
        Assert.assertFalse(eventRegister.isEventExist(eventID));
        Assert.assertFalse(eventRegister.getAllEvents()
                .stream()
                .anyMatch(event -> event.getEventID().equals(eventID)));
        eventRegister.deletePeriodAndEvents(periodID);
    }

    @Test
    public void changePeriod() {
        Instant start = Instant.parse("2017-09-18T00:01:37.907Z");
        Instant end = Instant.parse("2022-10-18T00:02:06.907Z");
        Instant secondEnd = Instant.parse("2022-10-18T00:02:07.907Z");

        Integer periodID = eventRegister.addPeriod(start, end);
        Integer periodForChangeID = eventRegister.addPeriod(start, secondEnd);
        
        Assert.assertNotEquals(periodForChangeID, periodID);
        
        Integer eventID = eventRegister.addEvent(100, periodID);
        eventRegister.changeEventPeriod(eventID, periodForChangeID);

        List<Event> allEvents = eventRegister.getAllEvents();
        boolean isExistEventWithOldPeriod = allEvents
                .stream()
                .anyMatch(event -> event.getEventID().equals(eventID) && event.getPeriodID().equals(periodID));
        Assert.assertFalse(isExistEventWithOldPeriod);

        boolean isExistEventWithActualPeriod = allEvents
                .stream()
                .anyMatch(event -> event.getEventID().equals(eventID) && event.getPeriodID().equals(periodForChangeID));
        Assert.assertTrue(isExistEventWithActualPeriod);

        eventRegister.deleteEvent(eventID);
        eventRegister.deletePeriodAndEvents(periodID);
        eventRegister.deletePeriodAndEvents(periodForChangeID);
    }
}
