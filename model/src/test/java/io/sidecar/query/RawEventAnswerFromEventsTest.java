package io.sidecar.query;

import static org.testng.Assert.assertEquals;

import java.util.Collections;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import io.sidecar.event.Event;
import io.sidecar.event.KeyTag;
import io.sidecar.event.Reading;
import io.sidecar.geo.Location;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.testng.annotations.Test;

public class RawEventAnswerFromEventsTest {

    @Test(description = "Asserts that a RawEventAnswer can be created from source events")
    public void answerFromEvents() {
        DateTime dateTime = DateTime.now(DateTimeZone.UTC);

        Event.Builder builder = new Event.Builder()
                .deviceId(UUID.randomUUID())
                .timestamp(dateTime)
                .stream("stream")
                .tags(Sets.newHashSet("tag"))
                .location(new Location(42.4, 95.5))
                .keytags(ImmutableList.of(new KeyTag("key", Sets.newHashSet("keytag"))));

        Event eventA = builder.id(UUID.randomUUID()).readings(
                Collections.singletonList(new Reading("foo", dateTime, 23))).build();

        Event eventB = builder.id(UUID.randomUUID()).readings(
                Collections.singletonList(new Reading("foo", dateTime, 42))).build();

        RawEventsAnswer answer = RawEventsAnswer.fromEvents(eventA, eventB);
        assertEquals(answer.getEvents(), ImmutableList.of(eventA, eventB));
    }

}
