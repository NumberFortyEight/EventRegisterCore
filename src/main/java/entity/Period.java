package entity;

import java.util.Date;
import java.util.Objects;

public class Period {
    private Date dateBegin;
    private Date dateEnd;

    public Period(Date dateBegin, Date dateEnd) {
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
    }

    public Period() {
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
