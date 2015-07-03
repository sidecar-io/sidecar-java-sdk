package io.sidecar.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.sidecar.jackson.ModelMapper;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class NotificationJsonValidationTest {

    ObjectMapper mapper = new ModelMapper();

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
