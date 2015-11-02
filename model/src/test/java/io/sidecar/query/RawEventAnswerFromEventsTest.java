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
