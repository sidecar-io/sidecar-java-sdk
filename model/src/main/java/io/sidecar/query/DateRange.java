package io.sidecar.query;

import com.google.common.base.Preconditions;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public class DateRange {

    public final DateTime begin;
    public final DateTime end;
    public final Interval duration;

    public DateRange(final DateTime begin, final DateTime end) {
        this.begin = begin;
        this.end = end;
        Preconditions.checkArgument(end.isAfter(begin));

        this.duration = new Interval(this.begin, this.end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DateRange dateRange = (DateRange) o;

        if (!begin.equals(dateRange.begin)) {
            return false;
        }
        if (!duration.equals(dateRange.duration)) {
            return false;
        }
        if (!end.equals(dateRange.end)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = begin.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + duration.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DateRange {" +
               "begin=" + begin +
               ", end=" + end +
               "} " + super.toString();
    }
}