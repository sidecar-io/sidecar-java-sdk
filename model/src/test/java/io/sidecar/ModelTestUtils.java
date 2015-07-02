package io.sidecar;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.sidecar.credential.Credential;
import io.sidecar.event.Event;
import io.sidecar.event.Reading;
import io.sidecar.geo.Location;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Collection;
import java.util.UUID;

public class ModelTestUtils {

    private ModelTestUtils() {
    }

    public static Event createSampleEvent() {
        Reading reading = new Reading("ABC", DateTime.now(DateTimeZone.UTC), 42);
        return new Event.Builder()
                .id(UUID.randomUUID())
                .deviceId(UUID.randomUUID())
                .timestamp(DateTime.now(DateTimeZone.UTC))
                .stream("wowza")
                .tags(ImmutableSet.<String>of())
                .location(new Location(47.6014, -122.33))
                .readings(ImmutableList.of(reading))
                .keyTags(ImmutableMap.<String, Collection<String>>of())
                .build();
    }

    public static Credential createSampleCredential() {
        return new Credential("username", "password");
    }

}
