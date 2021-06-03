import core.EventRegister;
import core.EventRegisterImpl;
import entity.Period;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.List;

public class PeriodsTest {

    private final EventRegister eventRegister =
            new EventRegisterImpl("C:\\Users\\user\\Desktop\\Рабочие папки\\datasource.properties");

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
    @Test
    public void periodsForAPeriod(){
        Integer period3 = eventRegister.addPeriod(Instant.parse("2009-09-18T00:01:37.907Z"), Instant.parse("2010-10-19T00:02:07.907Z"));
        Integer period1 = eventRegister.addPeriod(Instant.parse("2019-09-18T00:01:37.907Z"), Instant.parse("2020-10-18T00:02:06.907Z"));
        Integer period2 = eventRegister.addPeriod(Instant.parse("2018-09-18T00:01:37.907Z"), Instant.parse("2021-09-18T00:03:06.907Z"));
        Integer period4 = eventRegister.addPeriod(Instant.parse("2012-09-18T00:01:37.907Z"), Instant.parse("2025-10-18T00:02:06.907Z"));
        Integer period5 = eventRegister.addPeriod(Instant.parse("2019-09-18T00:01:37.907Z"), Instant.parse("2027-10-18T00:02:06.907Z"));
        Integer period6 = eventRegister.addPeriod(Instant.parse("2011-09-18T00:01:37.907Z"), Instant.parse("2028-10-18T00:02:06.907Z"));

        List<Period> periodsForAPeriod = eventRegister.getPeriodsForAPeriod(Instant.parse("2009-09-18T00:01:37.907Z"), Instant.parse("2028-10-18T00:02:06.907Z"));
        Assert.assertTrue(periodsForAPeriod.stream().anyMatch(period -> period.getPeriodID().equals(period1)));
        Assert.assertTrue(periodsForAPeriod.stream().anyMatch(period -> period.getPeriodID().equals(period2)));
        Assert.assertTrue(periodsForAPeriod.stream().anyMatch(period -> period.getPeriodID().equals(period3)));
        Assert.assertTrue(periodsForAPeriod.stream().anyMatch(period -> period.getPeriodID().equals(period4)));
        Assert.assertTrue(periodsForAPeriod.stream().anyMatch(period -> period.getPeriodID().equals(period5)));
        Assert.assertTrue(periodsForAPeriod.stream().anyMatch(period -> period.getPeriodID().equals(period6)));


        List<Period> periodsForAPeriod1 = eventRegister.getPeriodsForAPeriod(Instant.parse("2012-09-18T00:01:37.907Z"), Instant.parse("2028-10-18T00:02:06.907Z"));
        Assert.assertTrue(periodsForAPeriod1.stream().anyMatch(period -> period.getPeriodID().equals(period1)));
        Assert.assertTrue(periodsForAPeriod1.stream().anyMatch(period -> period.getPeriodID().equals(period2)));
        Assert.assertTrue(periodsForAPeriod1.stream().anyMatch(period -> period.getPeriodID().equals(period4)));
        Assert.assertTrue(periodsForAPeriod1.stream().anyMatch(period -> period.getPeriodID().equals(period5)));

        List<Period> periodsForAPeriod2 = eventRegister.getPeriodsForAPeriod(Instant.parse("2019-09-18T00:01:37.907Z"), Instant.parse("2020-10-18T00:02:06.907Z"));
        Assert.assertTrue(periodsForAPeriod2.stream().anyMatch(period -> period.getPeriodID().equals(period1)));

        List<Period> periodsForAPeriod3 = eventRegister.getPeriodsForAPeriod(Instant.parse("2009-09-18T00:01:37.907Z"), Instant.parse("2010-10-19T00:02:07.907Z"));
        Assert.assertTrue(periodsForAPeriod3.stream().anyMatch(period -> period.getPeriodID().equals(period3)));

        eventRegister.deletePeriodAndEvents(period1);
        eventRegister.deletePeriodAndEvents(period2);
        eventRegister.deletePeriodAndEvents(period3);
        eventRegister.deletePeriodAndEvents(period5);
        eventRegister.deletePeriodAndEvents(period5);
        eventRegister.deletePeriodAndEvents(period6);
    }

}
