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

package io.sidecar;

import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import io.sidecar.credential.Credential;
import io.sidecar.event.Event;
import io.sidecar.event.KeyTag;
import io.sidecar.event.Reading;
import io.sidecar.geo.Location;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

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
                .keytags(ImmutableList.of(new KeyTag("key", Sets.newHashSet("keytag"))))
                .build();
    }

    public static Credential createSampleCredential() {
        return new Credential("username", "password");
    }

}
