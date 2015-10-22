/*
 * Copyright 2015 Sidecar.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.sidecar.query;

import io.sidecar.geo.Location;

public class GeoDistance {

    public final double lat;
    public final double lon;
    public final String distance;

    public GeoDistance(Location location, final String distance) {
        this.lat = location.getLat();
        this.lon = location.getLon();
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GeoDistance that = (GeoDistance) o;

        if (Double.compare(that.lat, lat) != 0) {
            return false;
        }
        if (Double.compare(that.lon, lon) != 0) {
            return false;
        }
        if (!distance.equals(that.distance)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(lat);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lon);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + distance.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "GeoDistance {" +
               "lat=" + lat +
               ", log=" + lon +
               ", distance=" + distance +
               "} " + super.toString();
    }
}
