import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class passListTest {

    private final EventRegister eventRegister = null;

    @Test
    public void passlist(){
        int userID = 1337;
        Instant start = Instant.parse("2019-09-18T00:01:37.907Z");
        Instant end = Instant.parse("2020-10-18T00:02:06.907Z");

        Integer periodID = eventRegister.addPeriod(start, end);
        Integer eventID = eventRegister.addEvent(1000, periodID);
        eventRegister.registerUserToEvent(userID, eventID);
        Assert.assertTrue(eventRegister.getAllEvents()
                .stream()
                .anyMatch(event -> event.getEventID().equals(eventID) && event.getPeriodID().equals(periodID)));
        Assert.assertTrue(eventRegister.isUserRegistered(userID, eventID));
        eventRegister.deleteEvent(eventID);
        Assert.assertFalse(eventRegister.getAllEvents()
                .stream()
                .anyMatch(event -> event.getEventID().equals(eventID) && event.getPeriodID().equals(periodID)));
        eventRegister.deletePeriodAndEvents(periodID);
    }
    @Test
    public void isUserCanEnter(){
        int userID = 1337;
        Instant start = Instant.parse("2017-09-18T00:01:37.907Z");
        Instant end = Instant.parse("2021-10-18T00:02:06.907Z");

        Integer periodID = eventRegister.addPeriod(start, end);
        Integer eventID = eventRegister.addEvent(1000, periodID);
        eventRegister.registerUserToEvent(userID, eventID);

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, 2019);
        calendar.set(Calendar.MONTH, Calendar.AUGUST);
        calendar.set(Calendar.DATE, 5);
        calendar.set(Calendar.HOUR, 11);
        calendar.set(Calendar.MINUTE, 20);
        Instant trueEnterTime = calendar.toInstant();
        Assert.assertTrue(eventRegister.canUserEnter(userID, eventID, trueEnterTime));
        Assert.assertTrue(eventRegister.canUserEnter(userID, eventID, start));
        Assert.assertTrue(eventRegister.canUserEnter(userID, eventID, end));

        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        Instant earlyEnterTime = calendar.toInstant();
        Assert.assertFalse(eventRegister.canUserEnter(userID, eventID, earlyEnterTime));

        calendar.set(Calendar.YEAR, 2022);
        calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
        Instant pastTime = calendar.toInstant();
        Assert.assertFalse(eventRegister.canUserEnter(userID, eventID, pastTime));

        eventRegister.deletePeriodAndEvents(periodID);
        eventRegister.deleteEvent(eventID);
    }
    @Test
    public void activityTest(){
        int userID = 1337;
        Instant start = Instant.parse("2017-09-18T00:01:37.907Z");
        Instant end = Instant.parse("2021-10-18T00:02:06.907Z");

        Integer periodID = eventRegister.addPeriod(start, end);
        Integer eventID = eventRegister.addEvent(1000, periodID);
        Integer secondEventID = eventRegister.addEvent(2000, periodID);
        eventRegister.registerUserToEvent(userID, eventID);
        eventRegister.registerUserToEvent(userID, secondEventID);

        Assert.assertTrue(eventRegister.isUserRegistered(userID, eventID));
        Assert.assertTrue(eventRegister.isUserRegistered(userID, secondEventID));
        eventRegister.disableUser(userID);
        Assert.assertFalse(eventRegister.isUserRegistered(userID, eventID));
        Assert.assertFalse(eventRegister.isUserRegistered(userID, secondEventID));
        eventRegister.activateUser(userID);
        Assert.assertTrue(eventRegister.isUserRegistered(userID, eventID));
        Assert.assertTrue(eventRegister.isUserRegistered(userID, secondEventID));
        eventRegister.disableEvent(eventID);
        eventRegister.disableEvent(secondEventID);
        Assert.assertFalse(eventRegister.isUserRegistered(userID, eventID));
        Assert.assertFalse(eventRegister.isUserRegistered(userID, secondEventID));
        eventRegister.activateEvent(eventID);
        eventRegister.activateEvent(secondEventID);
        Assert.assertTrue(eventRegister.isUserRegistered(userID, eventID));
        Assert.assertTrue(eventRegister.isUserRegistered(userID, secondEventID));

        eventRegister.deletePeriodAndEvents(periodID);
        eventRegister.deleteEvent(eventID);
        eventRegister.deleteEvent(secondEventID);
    }
}
