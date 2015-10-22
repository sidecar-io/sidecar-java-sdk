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

package io.sidecar.access;

import org.testng.annotations.Test;

import io.sidecar.jackson.ModelMapper;

import static org.testng.Assert.assertEquals;

public class AccessKeyJsonValidationTest {

    ModelMapper mapper = new ModelMapper();

    @Test(description = "Asserts that serializing an AccessKey to json and then deserializing it again produces an "
          + "equal object")
    public void serializeAndDeserialize() throws Exception {
        AccessKey original = new AccessKey.Builder()
              .keyId("SFOFWRSON72LTDXS0G8H")
              .secret("hkajslhdflkjashflkajfhaf")
              .build();

        String json = mapper.writeValueAsString(original);
        AccessKey deserialized = mapper.readValue(json, AccessKey.class);

        assertEquals(original, deserialized);
    }
}
