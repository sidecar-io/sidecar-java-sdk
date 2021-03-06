/*
 * Copyright 2015 QSense, Inc.
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
import static com.google.common.base.Preconditions.checkNotNull;
import static io.sidecar.ModelUtils.isValidDeviceId;
import static io.sidecar.util.CollectionUtils.filterNulls;

import java.util.Arrays;
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
    private final String deviceId;
    private final DateTime timestamp;
    private final String stream;
    private final Location location;
    private final ImmutableList<Reading> readings;
    private final ImmutableSet<String> tags;
    private final ImmutableList<KeyTag> keytags;


    private Event(Builder b) {
        this(b.id, b.deviceId, b.timestamp, b.stream, b.tags, b.location, b.readings, b.keytags);
    }

    private Event(UUID id, String deviceId, DateTime timestamp, String stream,
                  Collection<String> tags, Location location, List<Reading> readings,
                  List<KeyTag> keytags) {
        this.id = id;

        checkArgument(isValidDeviceId(deviceId),
                "Invalid DeviceId of %s - must be between 8-40 valid characters", deviceId);
        this.deviceId = deviceId;

        checkNotNull(timestamp, "ts must be present");
        this.timestamp = new DateTime(timestamp, DateTimeZone.UTC);

        checkArgument(ModelUtils.isValidStreamId(stream), "stream must be non-blank and must not contain whitespace");
        this.stream = stream.toLowerCase();

        checkNotNull(location, "location must be present");
        this.location = location;

        checkNotNull(readings, "readings must be present");
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
        }), "tags or keytags must contain no whitespace characters");
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
    public String getDeviceId() {
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

    /**
     * Tags are optional user defined metadata. A tag should not contain whitespace.
     *
     *
     * @return an array of valid tags (no whitespace)
     */
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
     * Get the location (latitude and longitude) of the origin of this Event.
     *
     * @return geo-location latitude and longitude
     */
    public Location getLocation() {
        return location;
    }

    /**
     * An Optional list of {@link KeyTag}s where each entry in the List corresponds to a set of tags to be applied to
     * the individual keys found the readings encapsulated by this Event.
     * @return a List of Keytags, or an empty list if there are no keytags
     */
    public ImmutableList<KeyTag> getKeytags() {
        return (keytags != null) ? keytags : ImmutableList.<KeyTag>of();
    }

    @SuppressWarnings("unused")  //Used by jackson
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
        private String deviceId;
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

        public Builder deviceId(String deviceId) {
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

        @SuppressWarnings("unused")
        public Builder readings(Reading... readings) {
            this.readings = Arrays.asList(readings);
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
