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
