package io.sidecar.event;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.sidecar.jackson.ModelMapper;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.Assert.assertEquals;

public class EventWithIdsJsonValidationTest {

    private UUID appIdUnderTest = UUID.randomUUID();
    private Event eventUnderTest;
    private ObjectNode eventWithAppIdAsJson;
    private ModelMapper mapper = new ModelMapper();

    @BeforeMethod
    public void loadValidEventJsonAsString() throws Exception {
        String validEvent = IOUtils.toString(this.getClass().getResource("/proposed_event.json"));
        ObjectNode eventAsJson = (ObjectNode) mapper.readTree(validEvent);
        eventUnderTest = mapper.readValue(eventAsJson.traverse(), Event.class);

        eventWithAppIdAsJson = JsonNodeFactory.instance.objectNode();
        eventWithAppIdAsJson.set("event", eventAsJson);
        eventWithAppIdAsJson.put("appId", appIdUnderTest.toString());
        eventWithAppIdAsJson.put("orgId", appIdUnderTest.toString());
        eventWithAppIdAsJson.put("userId", appIdUnderTest.toString());

    }

    @Test(description =
          "Assert that an eventWithIds json object can be created when appIdUnderTest and Event fields"
          + "are valid")

    public void eventWithIdsIsCreatedWithAllMandatoryFields() throws Exception {
        EventWithIds eventWithIds = mapper.readValue(eventWithAppIdAsJson.traverse(), EventWithIds.class);

        assertEquals(eventWithIds.getAppId(), appIdUnderTest);
        assertEquals(eventWithIds.getOrgId(), appIdUnderTest);
        assertEquals(eventWithIds.getEvent(), eventUnderTest);
        assertEquals(eventWithIds.getUserId(), appIdUnderTest);
    }

    @Test(description = "Assert that event.ts does not change after serializing and deserializing again")
    public void tsValueDoesNotChange() throws Exception {
        EventWithIds eventWithIds = mapper.readValue(eventWithAppIdAsJson.traverse(), EventWithIds.class);
        DateTime expected = eventWithIds.getEvent().getTimestamp();

        String serialized = mapper.writeValueAsString(eventWithIds);
        EventWithIds deserialized = mapper.readValue(serialized, EventWithIds.class);

        assertEquals(deserialized.getEvent().getTimestamp(), expected);
    }
}
