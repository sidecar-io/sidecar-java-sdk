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
