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

package io.sidecar.org;

import org.testng.annotations.Test;

import java.util.UUID;

import io.sidecar.jackson.ModelMapper;

import static org.testng.Assert.assertEquals;

public class UserGroupJsonValidationTest {

    private ModelMapper mapper = new ModelMapper();

    @Test(description = "Assert that serializing to json and deserializing back again produces an equal " +
            "UserGroup object")
    public void toAndFromJsonProducesEqual() throws Exception {
        UserGroup original = new UserGroup.Builder()
                .id(UUID.randomUUID())
                .appendMember(new UserGroupMember(UUID.randomUUID(), UserGroupRole.ADMIN))
                .appendDeviceId(UUID.randomUUID())
                .appId(UUID.randomUUID())
                .name("Test Group")
                .build();

        String asJson = mapper.writeValueAsString(original);

        UserGroup deserialized = mapper.readValue(asJson, UserGroup.class);
        assertEquals(deserialized, original);
    }


}
