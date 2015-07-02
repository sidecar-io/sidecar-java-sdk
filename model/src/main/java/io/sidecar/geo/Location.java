package io.sidecar.geo;

import com.google.common.base.Preconditions;


public class Location {

    final private double lat;
    final private double lon;

    public Location(double lat, double lon) {
        Preconditions.checkArgument(lat >= -90 && lat <= 90);
        Preconditions.checkArgument(lon >= -180 && lon <= 180);

        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Location that = (Location) o;

        return this.lat == that.lat &&
               this.lon == that.lon;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(lat);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lon);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Location{" +
               "lat=" + lat +
               ", lon=" + lon +
               "} " + super.toString();
    }
}
