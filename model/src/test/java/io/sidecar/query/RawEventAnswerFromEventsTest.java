package io.sidecar.query;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import io.sidecar.event.Event;
import io.sidecar.event.Reading;
import io.sidecar.geo.Location;

import static org.testng.Assert.assertEquals;

public class RawEventAnswerFromEventsTest {

    @Test(description = "Asserts that a RawEventAnswer can be created from source events")
    public void answerFromEvents() {
        DateTime dateTime = DateTime.now(DateTimeZone.UTC);

        Event.Builder builder = new Event.Builder()
              .deviceId(UUID.randomUUID())
              .timestamp(dateTime )
              .stream("stream")
              .tags(Sets.newHashSet("tag"))
              .location(new Location(42.4, 95.5))
              .keyTags(ImmutableMap.<String,Collection<String>>of());

        Event eventA = builder.id(UUID.randomUUID()).readings(
              Collections.singletonList(new Reading("foo", dateTime, 23))).build();

        Event eventB = builder.id(UUID.randomUUID()).readings(
              Collections.singletonList(new Reading("foo", dateTime, 42))).build();

        RawEventsAnswer answer = RawEventsAnswer.fromEvents(eventA, eventB);
        assertEquals(answer.getEvents(), ImmutableList.of(eventA, eventB));
    }

}
