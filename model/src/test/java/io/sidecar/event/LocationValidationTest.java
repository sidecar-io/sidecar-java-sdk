package io.sidecar.event;

import org.testng.annotations.Test;

import io.sidecar.geo.Location;

import static org.testng.Assert.assertEquals;

public class LocationValidationTest {

    @Test(description = "Asserts that a Location can be created with proper latitude and longitude")
    public void withValidLatLong() {
        double latitude = 55.0005322;
        double longitude = -129.223499;

        Location location = new Location(latitude, longitude);
        assertEquals(location.getLat(), latitude);
        assertEquals(location.getLon(), longitude);
    }

    @Test(description = "Asserts that a Location can't be created with latitude < -90 ",
          expectedExceptions = IllegalArgumentException.class)
    public void withLatLessThanNeg90() {
        double latitude = -90.0000001;
        double longitude = -129.223499;

        new Location(latitude, longitude);
    }

    @Test(description = "Asserts that a Location can't be created with latitude > 90",
          expectedExceptions = IllegalArgumentException.class)
    public void withLatGreaterThan90() {
        double latitude = 90.0000001;
        double longitude = -129.223499;

        new Location(latitude, longitude);
    }

    @Test(description = "Asserts that a Location can't be created with longitude < -180",
          expectedExceptions = IllegalArgumentException.class)
    public void withLongLessThanNeg180() {
        double latitude = 55.0005322;
        double longitude = -180.223499;

        new Location(latitude, longitude);
    }

    @Test(description = "Asserts that a Location can't be created with longitude > 180",
          expectedExceptions = IllegalArgumentException.class)
    public void withLongGreaterThan180() {
        double latitude = 55.0005322;
        double longitude = 180.223499;

        new Location(latitude, longitude);
    }


}
