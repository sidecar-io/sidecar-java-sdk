package io.sidecar.event;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Optional;
import org.joda.time.DateTime;


@SuppressWarnings("unused") //Used by Jackson
abstract class ReadingMixin {

    private ReadingMixin(@JsonProperty("key") String key,
                         @JsonProperty("ts") DateTime timestamp,
                         @JsonProperty("value") double value) {
    }

    @JsonIgnore
    abstract Optional<DateTime> getTimestamp();

    @JsonGetter("ts")
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    abstract String getTimestampAsIso8601String();
}
