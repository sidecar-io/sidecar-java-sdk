package io.sidecar.event;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.sidecar.geo.Location;
import org.joda.time.DateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused") //Used by Jackson
abstract class EventMixin {

    private EventMixin(@JsonProperty("id") UUID id,
                       @JsonProperty("deviceId") UUID deviceId,
                       @JsonProperty("ts") DateTime timestamp,
                       @JsonProperty("stream") String streamName,
                       @JsonProperty("tags") Collection<String> tags,
                       @JsonProperty("location") Location location,
                       @JsonProperty("readings") List<Reading> readings,
                       @JsonProperty("keyTags") List<KeyTag> keyTags) {
    }

    @JsonIgnore
    abstract DateTime getTimestamp();

    @JsonGetter("stream")
    abstract String getStreamName();

    @JsonGetter("ts")
    abstract String getTimestampAsIso8601String();
}
