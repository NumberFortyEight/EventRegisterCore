package entity;

import java.util.Date;
import java.util.Objects;

public class Period {
    private Integer periodID;
    private Date dateBegin;
    private Date dateEnd;

    public Period() {
    }

    public Period(Integer periodID, Date dateBegin, Date dateEnd) {
        this.periodID = periodID;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return Objects.equals(dateBegin, period.dateBegin) && Objects.equals(dateEnd, period.dateEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateBegin, dateEnd);
    }

    public Integer getPeriodID() {
        return periodID;
    }

    public void setPeriodID(Integer periodID) {
        this.periodID = periodID;
    }

    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }
}
