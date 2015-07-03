package io.sidecar.query;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.joda.time.DateTime;

import io.sidecar.geo.Location;

@SuppressWarnings("unused") //Used by Jackson
abstract class DataPointMixin {

    private DataPointMixin(@JsonProperty("ts") DateTime timestamp,
                           @JsonProperty("value") double value,
                           @JsonProperty("location") Location location) {
    }


    @SuppressWarnings("unused") //This is required to exist for Mixins during serialization
    @JsonGetter("ts")
    abstract String getTimestampAsIso8601String();

    @JsonIgnore
    abstract DateTime getTimestamp();

}
