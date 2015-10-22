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

package io.sidecar;


import org.testng.annotations.Test;

import io.sidecar.jackson.ModelMapper;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;

public class ErrorJsonValidationTest {

    @Test(description = "Asserts that Errors can be deserialized and serialized back in to " +
            "an equal, but not the same, object")
    public void toJsonAndBack() throws Exception {
        ModelMapper mapper = new ModelMapper();
        Error original = new Error("ABC", "123");
        String serialized = mapper.writeValueAsString(original);
        Error deserialized = mapper.readValue(serialized, Error.class);

        assertNotSame(deserialized, original);
        assertEquals(deserialized, original);
    }
}
