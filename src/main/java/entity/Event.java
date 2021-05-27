package entity;

@SuppressWarnings("unused")
public class Event {
    private Integer eventID;
    private Integer locationID;
    private Integer periodID;

    public Event(Integer eventID, Integer locationID, Integer periodID) {
        this.eventID = eventID;
        this.locationID = locationID;
        this.periodID = periodID;
    }

    public Event() {
    }

    public Integer getEventID() {
        return eventID;
    }

    public void setEventID(Integer eventID) {
        this.eventID = eventID;
    }

    public Integer getLocationID() {
        return locationID;
    }

    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

    public Integer getPeriodID() {
        return periodID;
    }

    public void setPeriodID(Integer periodID) {
        this.periodID = periodID;
    }
}
