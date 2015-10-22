/*
 * Copyright 2015 Sidecar.io
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

import io.sidecar.jackson.ModelMapper;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

import static org.joda.time.DateTimeZone.UTC;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotSame;


public class ReadingJsonValidation {

    ModelMapper mapper = new ModelMapper();

    @Test(description = "Asserts that Reading can be deserialized and serialized back in to an equals, but not the " +
            "same, object")
    public void toJsonAndBack() throws Exception {
        Reading original = new Reading("ABC", DateTime.now(UTC), 42);
        String serialized = mapper.writeValueAsString(original);
        Reading deserialized = mapper.readValue(serialized, Reading.class);

        assertNotSame(deserialized, original);
        assertEquals(deserialized, original);
    }

    @Test(description = "Assert that Reading can be serialized / deserialized without timestamp")
    public void toJsonAndBackNoTs() throws Exception {
        Reading original = new Reading("ABC", null, 42);
        String serialized = mapper.writeValueAsString(original);
        Reading deserialized = mapper.readValue(serialized, Reading.class);

        assertNotSame(deserialized, original);
        assertEquals(deserialized, original);
    }


    @Test(description = "Assert that Reading serialized does not have a timestamp field")
    public void toJsonNoTs() throws Exception {
        Reading original = new Reading("ABC", null, 42);
        assertFalse(mapper.readTree(mapper.writeValueAsString(original)).has("ts"));
    }
}
