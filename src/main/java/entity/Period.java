package entity;

import java.time.Instant;

public class Period {
    private Integer periodID;
    private Instant startDate;
    private Instant endDate;

    public Period(Integer periodID, Instant startDate, Instant endDate) {
        this.periodID = periodID;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Period() {
    }

    public Integer getPeriodID() {
        return periodID;
    }

    public void setPeriodID(Integer periodID) {
        this.periodID = periodID;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }
}
