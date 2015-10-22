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

package io.sidecar.query;

import com.fasterxml.jackson.databind.JsonNode;
import io.sidecar.event.Event;
import io.sidecar.jackson.ModelMapper;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.Assert.assertTrue;

public class UserAnswerBucketJsonValidationTest {

    private ModelMapper mapper = new ModelMapper();

    @Test(description = "Asserts that serializing a UserAnswerBucket containing RawEventsAnswers produces the proper" +
            "json")
    public void testRawEventsAnswerBucketSerialization() throws Exception {
        JsonNode expectedJson = mapper.readTree(this.getClass().getResource("/raw_events_answer_bucket_response.json"));

        Event event = mapper.readValue(expectedJson.path("answer").path("events").get(0).traverse(), Event.class);

        RawEventsAnswer rawEventsAnswer = RawEventsAnswer.fromEvents(event);
        UserAnswerBucket<RawEventsAnswer> uab = new UserAnswerBucket<RawEventsAnswer>(UUID.fromString(expectedJson.path("userId").asText()),
                UUID.fromString(expectedJson.path("deviceId").asText()), rawEventsAnswer);

        JsonNode actualJson = mapper.readTree(mapper.writeValueAsBytes(uab));

        assertTrue(actualJson.equals(expectedJson));
    }
}
