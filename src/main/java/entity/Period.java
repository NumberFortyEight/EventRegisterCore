package entity;

import java.time.Instant;

@SuppressWarnings("unused")
public class Period {
    private Integer periodID;
    private Instant startDate;
    private Instant endDate;

    public Period(Integer periodID, Instant startDate, Instant endDate) {
        this.periodID = periodID;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @SuppressWarnings("unused")
    public Period() {
    }

    public Integer getPeriodID() {
        return periodID;
    }

    @SuppressWarnings("unused")
    public void setPeriodID(Integer periodID) {
        this.periodID = periodID;
    }

    @SuppressWarnings("unused")
    public Instant getStartDate() {
        return startDate;
    }

    @SuppressWarnings("unused")
    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    @SuppressWarnings("unused")
    public Instant getEndDate() {
        return endDate;
    }

    @SuppressWarnings("unused")
    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }
}
