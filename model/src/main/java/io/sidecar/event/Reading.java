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

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Objects;

import com.google.common.base.Optional;
import io.sidecar.ModelUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;


public final class Reading {

    private final String key;
    private final DateTime timestamp;
    private final double value;

    /**
     * Create a Reading from a key, a timestamp, and a value.
     *
     * @param key       - A non-null String that contains no whitespace.
     * @param timestamp - timestamp for the event (ie, 1970-01-01T00:00:00Z)
     * @param value     - A value for the event.  All double floating precision values are legal.
     */
    public Reading(String key, DateTime timestamp, double value) {
        checkArgument(ModelUtils.isValidReadingKey(key), key + " is not a valid key.");
        this.key = key;

        this.timestamp = (timestamp == null) ? null : new DateTime(timestamp, DateTimeZone.UTC);

        this.value = value;
    }

    public Reading(String key, double value) {
        this(key, null, value);
    }

    public String getKey() {
        return key;
    }

    /**
     * An optional timestamp for a reading.
     *
     * @return yyyy-MM-ddThh:mm:ssZ -- A date-time with a time-zone in the ISO-8601 calendar format
     */
    public Optional<DateTime> getTimestamp() {
        return Optional.fromNullable(timestamp);
    }

    @SuppressWarnings("unused") //This is required to exist for Mixins during serialization
    private String getTimestampAsIso8601String() {
        return (timestamp == null) ? null : timestamp.toString();
    }

    /**
     * The value associated with the readings key
     *
     * @return The value as a double
     */
    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reading reading = (Reading) o;
        return Objects.equals(value, reading.value) &&
                Objects.equals(key, reading.key) &&
                Objects.equals(timestamp, reading.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, timestamp, value);
    }

    @Override
    public String toString() {
        return "Reading{" +
                "key='" + key + '\'' +
                ", timestamp=" + timestamp +
                ", value=" + value +
                '}';
    }
}
