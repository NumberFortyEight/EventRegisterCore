package core;

import entity.Event;
import entity.Period;

import java.time.Instant;
import java.util.List;
/** Каждый возвращающий метод может выбросить null в случе sql ошибки */
public interface EventRegister {

    Integer addPeriod(Instant startInstant, Instant endInstant);
    /**Вместе с периодом удаляются и все ивенты с id периода*/
    void deletePeriodAndEvents(Integer periodID);
    Boolean isPeriodExist(Integer periodID);
    List<Period> getAllPeriods();

    Integer addEvent(Integer locationID, Integer periodID);
    void deleteEvent(Integer eventID);
    void changeEventPeriod(Integer eventID, Integer periodID);
    void changeEventLocation(Integer eventID, Integer locationID);
    Boolean isEventExist(Integer eventID);
    Event getEventByID(Integer eventID);
    List<Event> getEventsByPeriod(Integer periodID);
    List<Event> getAllEvents();

    @SuppressWarnings("UnusedReturnValue")
    Boolean registerUserToEvent(Integer userID, Integer EventID);
    /**Зарегистрирован ли юзер на ивент */
    Boolean isUserRegistered(Integer userID, Integer EventID);
    /**Зарегистрирован юзер на ивент и может ли он попасть на мероприятие в указанное время (entryTime)*/
    Boolean canUserEnter(Integer userID, Integer EventID, Instant entryTime);

    void activateEvent(Integer eventID);
    void disableEvent(Integer eventID);

    void activateUser(Integer userID);
    void disableUser(Integer userID);

}
