package io.sidecar.query;

import com.google.common.base.Preconditions;

import org.joda.time.DateTime;

import io.sidecar.geo.Location;

public class DataPoint {

    final DateTime timestamp;
    final double value;
    final Location location;

    public DataPoint(DateTime timestamp, double value, Location location) {
        Preconditions.checkNotNull(timestamp);
        Preconditions.checkNotNull(location);

        this.timestamp = timestamp;
        this.value = value;
        this.location = location;
    }


    public double getValue() {
        return value;
    }

    public Location getLocation() {
        return location;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    private String getTimestampAsIso8601String() {
        return getTimestamp().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DataPoint dataPoint = (DataPoint) o;

        if (Double.compare(dataPoint.value, value) != 0) {
            return false;
        }
        if (!location.equals(dataPoint.location)) {
            return false;
        }
        if (!timestamp.equals(dataPoint.timestamp)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = timestamp.hashCode();
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + location.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DataPoint{" +
               "timestamp=" + timestamp +
               ", value=" + value +
               ", location=" + location +
               '}';
    }
}
