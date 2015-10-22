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

package io.sidecar.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.sidecar.jackson.ModelMapper;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class NotificationJsonValidationTest {

    ModelMapper mapper = new ModelMapper();

    String asJsonString;
    ObjectNode asObjectNode;

    @BeforeMethod
    public void loadValidEventJsonAsString() throws Exception {
        asJsonString = IOUtils.toString(this.getClass().getResource("/event_notification_sample.json"));
        asObjectNode = (ObjectNode) mapper.readTree(asJsonString);
    }

    @Test(description = "Asserts that deserializing expected json, then serializing and deserializing again, produces" +
            " equal objects.")
    public void deserializeAndSerializeProducesEquals() throws Exception {
        EventNotification deserialized = mapper.readValue(asObjectNode.traverse(), EventNotification.class);
        String serialized = mapper.writeValueAsString(deserialized);
        EventNotification deserializedAgain = mapper.readValue(serialized,EventNotification.class);

        assertEquals(deserialized, deserializedAgain);
    }


}
