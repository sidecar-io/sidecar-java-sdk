package io.sidecar.event;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.sidecar.util.CollectionUtils.filterNulls;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.google.common.base.CharMatcher;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.sidecar.ModelUtils;
import io.sidecar.geo.Location;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public final class Event {

    private final UUID id;
    private final UUID deviceId;
    private final DateTime timestamp;
    private final String stream;
    private final Location location;
    private final ImmutableList<Reading> readings;
    private final ImmutableSet<String> tags;
    private final ImmutableList<KeyTag> keytags;


    private Event(Builder b) {
        this(b.id, b.deviceId, b.timestamp, b.stream, b.tags, b.location, b.readings, b.keytags);
    }

    private Event(UUID id, UUID deviceId, DateTime timestamp, String stream,
                  Collection<String> tags, Location location, List<Reading> readings,
                  List<KeyTag> keytags) {
        checkNotNull(id);
        this.id = id;

        checkNotNull(deviceId);
        this.deviceId = deviceId;

        checkNotNull(timestamp);
        this.timestamp = new DateTime(timestamp, DateTimeZone.UTC);

        checkArgument(ModelUtils.isValidStreamId(stream));
        this.stream = stream.toLowerCase();

        checkNotNull(location);
        this.location = location;

        checkNotNull(readings);
        this.readings = filterNulls(readings);

        this.tags = (tags == null) ? null : filterBlankTags(tags);
        this.keytags = (keytags == null) ? null : filterBlankKeyTags(keytags);
        checkAllTagsHaveNoWhitespace();
        checkAllKeyTagsKeysAreValid();
    }

    private ImmutableList<KeyTag> filterBlankKeyTags(List<KeyTag> keytags) {
        ImmutableList.Builder<KeyTag> keyTagsBuilder = new ImmutableList.Builder<>();
        for (KeyTag entry : keytags) {
            ImmutableSet<String> tagsForKey = filterBlankTags(entry.getTags());
            keyTagsBuilder.add(new KeyTag(entry.getKey(), tagsForKey));
        }
        return keyTagsBuilder.build();
    }

    private ImmutableSet<String> filterBlankTags(Collection<String> tags) {
        return ImmutableSet.copyOf(Iterables.filter(tags, new Predicate<String>() {
            @Override
            public boolean apply(String s) {
                return StringUtils.isNotBlank(s);
            }
        }));
    }

    private void checkAllTagsHaveNoWhitespace() {
        List<String> allTags = Lists.newArrayList((tags != null) ? tags : Collections.<String>emptyList());
        for (KeyTag keyTag : (keytags != null) ? keytags : Collections.<KeyTag>emptyList() ) {
            allTags.addAll(keyTag.getTags());
        }
        checkArgument(Iterables.all(allTags, new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return CharMatcher.WHITESPACE.matchesNoneOf(input);
            }
        }));
    }

    private void checkAllKeyTagsKeysAreValid() {
        if (keytags == null) {
            return;
        }
        for (KeyTag keyTag : keytags) {
            checkArgument(ModelUtils.isValidReadingKey(keyTag.getKey()), keyTag.getKey() + " is not a valid key.");
        }
    }


    /**
     * A unique event identifier provided by the client.
     *
     * @return the event UUID as a string.
     */
    public UUID getId() {
        return id;
    }

    /**
     * The device identifier for the hardware, this value should be considered static (e.g., a MAC Address).
     *
     * @return the unique device identifier
     */
    public UUID getDeviceId() {
        return deviceId;
    }

    /**
     * The timestamp that the event was published from the client to sidecar
     *
     * @return yyyy-MM-ddThh:mm:ssZ -- A date-time with a time-zone in the ISO-8601 calendar format
     */
    public DateTime getTimestamp() {
        return timestamp;
    }

    @SuppressWarnings("unused") //This is required to exist for Mixins during serialization
    private String getTimestampAsIso8601String() {
        return getTimestamp().toString();
    }

    /**
     * Each event is mapped to a unique stream.
     *
     * @return the user defined stream name
     */
    public String getStream() {
        return stream;
    }

    public ImmutableSet<String> getTags() {
        return (tags != null) ? tags : ImmutableSet.<String>of();
    }

    @SuppressWarnings("unused")
    private Set<String> getTagsNull() {
        return tags;
    }

    /**
     * An array of one to many readings that the event encapsulates. Usually there is only
     * one reading per key name, but this is no enforced, allowing the Event to contain a "batch" of readings.
     * However, they will all share the parent Event timestamp and meta info.
     *
     * @return one to many keyed readings in an array
     */
    public ImmutableList<Reading> getReadings() {
        return readings;
    }

    /**
     * Optional fields, but if provided the properties can be used in the Query
     *
     * @return geo-location latitude and longitude
     */
    public Location getLocation() {
        return location;
    }

    public ImmutableList<KeyTag> getKeytags() {
        return (keytags != null) ? keytags : ImmutableList.<KeyTag>of();
    }

    @SuppressWarnings("unused")
    private List<KeyTag> getKeyTagsNull() {
        return keytags;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", deviceId=" + deviceId +
                ", timestamp=" + timestamp +
                ", stream='" + stream + '\'' +
                ", location=" + location +
                ", readings=" + readings +
                ", tags=" + tags +
                ", keytags=" + keytags +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Event that = (Event) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.deviceId, that.deviceId) &&
                this.timestamp.isEqual(that.timestamp) &&
                Objects.equals(this.stream, that.stream) &&
                Objects.equals(this.location, that.location) &&
                Objects.equals(this.readings, that.readings) &&
                Objects.equals(this.tags, that.tags) &&
                Objects.equals(this.keytags, that.keytags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deviceId, timestamp, stream, location, readings, tags, keytags);
    }

    public static class Builder {

        private UUID id;
        private UUID deviceId;
        private DateTime timestamp;
        private String stream;
        private Location location;
        private List<Reading> readings;
        private Collection<String> tags;
        private List<KeyTag> keytags;

        public Builder() {
        }

        public Builder(Event event) {
            this.id = event.id;
            this.deviceId = event.deviceId;
            this.timestamp = event.timestamp;
            this.stream = event.stream;
            this.location = event.location;
            this.readings = event.readings;
            this.tags = event.tags;
            this.keytags = event.keytags;
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder deviceId(UUID deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder timestamp(DateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder stream(String stream) {
            this.stream = stream;
            return this;
        }

        public Builder location(Location location) {
            this.location = location;
            return this;
        }

        public Builder readings(List<Reading> readings) {
            this.readings = readings;
            return this;
        }

        public Builder tags(Collection<String> tags) {
            this.tags = tags;
            return this;
        }

        public Builder keytags(List<KeyTag> keytags) {
            this.keytags = keytags;
            return this;
        }

        public Event build() {
            return new Event(this);
        }
    }
}
