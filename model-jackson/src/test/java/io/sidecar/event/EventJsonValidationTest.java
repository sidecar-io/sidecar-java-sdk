package io.sidecar.event;

import static org.testng.Assert.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.sidecar.jackson.ModelMapper;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * Test methods that assert that an Event object can only be created from a true valid json
 * payload.
 */
public class EventJsonValidationTest {

    private String validEventAsJsonString;
    private ObjectNode eventAsObjectNode;
    private ModelMapper mapper = new ModelMapper();

    static Reading readingFromJsonNode(JsonNode n) {
        return new Reading(n.path("key").asText(), new DateTime(n.path("ts").asText()),
                n.path("value").asLong());
    }

    @BeforeMethod
    public void loadValidEventJsonAsString() throws Exception {
        validEventAsJsonString =
                IOUtils.toString(this.getClass().getResource("/proposed_event.json"));
        eventAsObjectNode = (ObjectNode) mapper.readTree(validEventAsJsonString);
    }

    @Test(description = "Assert that an event json object can be created when the following mandatory fields are valid")
    public void eventIsCreatedWithAllMandatoryFields() throws Exception {
        Event event = mapper.readValue(validEventAsJsonString, Event.class);

        assertEquals(event.getId(), UUID.fromString(eventAsObjectNode.path("id").textValue()));
        assertEquals(event.getDeviceId(),
                UUID.fromString(eventAsObjectNode.path("deviceId").textValue()));
        assertEquals(event.getStream(), eventAsObjectNode.path("stream").asText());

        assertTrue(event.getTimestamp().isEqual(new DateTime(eventAsObjectNode.path("ts").textValue())));

        Set<String> tags = tagsJsonArrayToSet();
        assertEquals(event.getTags(), tags);

        List<Reading> readings = Lists.newArrayList(Iterables.transform(eventAsObjectNode.path("readings"),
                new Function<JsonNode, Reading>() {
                    @Override
                    public Reading apply(JsonNode input) {
                        return readingFromJsonNode(input);
                    }
                }));
        assertEquals(event.getReadings(), readings);
    }

    private Set<String> tagsJsonArrayToSet() {
        return Sets.newHashSet(Iterables.transform(eventAsObjectNode.path("tags"),
                new Function<JsonNode, String>() {
                    @Override
                    public String apply(JsonNode input) {
                        return input.textValue();
                    }
                }));
    }

    @Test(description = "Assert that an event can't be created when id isn't a UUID",
            expectedExceptions = JsonMappingException.class)
    public void invalidWhenEventIdIsNotUuid() throws Exception {
        eventAsObjectNode.put("id", "not-a-uuid");
        mapper.readValue(eventAsObjectNode.traverse(), Event.class);
    }

    @Test(description = "Assert that an event can't be created when id is missing",
            expectedExceptions = JsonMappingException.class)
    public void eventIdIsMissing() throws Exception {
        eventAsObjectNode.remove("id");
        mapper.readValue(eventAsObjectNode.traverse(), Event.class);
    }

    @Test(description = "Assert that an event can't be created when deviceId isn't a UUID",
            expectedExceptions = JsonMappingException.class)
    public void invalidWhenEventDeviceIdIsNotUuid() throws Exception {
        eventAsObjectNode.put("deviceId", "not-a-uuid");
        mapper.readValue(eventAsObjectNode.traverse(), Event.class);
    }

    @Test(description = "Assert that an event can't be created when deviceId is missing",
            expectedExceptions = JsonMappingException.class)
    public void eventDeviceIdIsMissing() throws Exception {
        eventAsObjectNode.remove("deviceId");
        mapper.readValue(eventAsObjectNode.traverse(), Event.class);
    }

    @Test(description = "Assert that handling timestamp with different timezone works")
    public void timestampIsPacificTime() throws Exception {
        DateTime dt = new DateTime(eventAsObjectNode.path("ts").asText()).withZone(DateTimeZone.UTC);
        eventAsObjectNode.put("ts", dt.toString());
        Event event = mapper.readValue(eventAsObjectNode.traverse(), Event.class);
        assertEquals(event.getTimestamp(), dt);
    }

    @Test(description = "Assert that event can't be created with missing timestamp",
            expectedExceptions = JsonMappingException.class)
    public void timestampIsMissing() throws Exception {
        eventAsObjectNode.remove("ts");
        mapper.readValue(eventAsObjectNode.traverse(), Event.class);
    }

    @Test(description = "Assert that event can't be created with non ISO-8601 timestamp",
            expectedExceptions = IllegalArgumentException.class)
    public void timestampIsInvalid() throws Exception {
        eventAsObjectNode.put("ts", "2014/04/04-12:00:00");
        mapper.readValue(eventAsObjectNode.traverse(), Event.class);
        throw new Exception("failure");
    }

    @Test(description = "Assert that event can't be created with a missing stream",
            expectedExceptions = JsonMappingException.class)
    public void streamIsMissing() throws Exception {
        eventAsObjectNode.remove("stream");
        mapper.readValue(eventAsObjectNode.traverse(), Event.class);
    }

    @Test(description = "Assert that event can't be created with a blank stream",
            expectedExceptions = JsonMappingException.class)
    public void streamIsBlank() throws Exception {
        eventAsObjectNode.put("stream", "   ");
        mapper.readValue(eventAsObjectNode.traverse(), Event.class);
    }

    @Test(description = "Assert that an event can be created with missing tags")
    public void tagsIsMissing() throws Exception {
        eventAsObjectNode.remove("tags");
        Event event = mapper.readValue(eventAsObjectNode.traverse(), Event.class);
        assertEquals(event.getTags(), ImmutableSet.<String>of());
    }

    @Test(description = "Assert that an event can have duplicate tags")
    public void tagsCanBeDuplicated() throws Exception {
        Set<String> originalTagSet = tagsJsonArrayToSet();
        ArrayNode tagsNode = (ArrayNode) eventAsObjectNode.path("tags");
        tagsNode.add(tagsNode.get(0));
        Event e = mapper.readValue(eventAsObjectNode.traverse(), Event.class);
        assertEquals(e.getTags(), originalTagSet);
    }

    @Test(description = "Assert that an event can have empty and blank tags, but those tags are ignored")
    public void tagsCanHaveEmptyOrBlankElements() throws Exception {
        Set<String> originalTagSet = tagsJsonArrayToSet();
        ((ArrayNode) eventAsObjectNode.path("tags")).add("  ").add("");
        Event e = mapper.readValue(eventAsObjectNode.traverse(), Event.class);
        assertEquals(e.getTags(), originalTagSet);
    }

    @Test(description = "Assert that the tags key can be null")
    public void tagsCanBeNull() throws Exception {
        eventAsObjectNode.set("tags", JsonNodeFactory.instance.nullNode());
        Event e = mapper.readValue(eventAsObjectNode.traverse(), Event.class);
        assertEquals(e.getTags(), ImmutableSet.of());
    }

    @Test(description = "Assert that the tags key can be missing")
    public void tagsCanBeMissing() throws Exception {
        eventAsObjectNode.remove("tags");
        Event e = mapper.readValue(eventAsObjectNode.traverse(), Event.class);
        assertEquals(e.getTags(), ImmutableSet.of());
    }

    @Test(description = "Assert that the tags key is excluded if empty when deserializing")
    public void serializationRemovesNullTags() throws Exception {
        eventAsObjectNode.remove("tags");
        Event e = mapper.readValue(eventAsObjectNode.traverse(), Event.class);

        JsonNode serialized = mapper.readTree(mapper.writeValueAsBytesUnchecked(e));
        JsonNode tagsPath = serialized.path("tags");
        assertTrue(MissingNode.getInstance().equals(tagsPath));
    }

    @Test(description = "Assert that the tags key is excluded if empty when deserializing")
    public void serializationRemovesNullKeyTags() throws Exception {
        eventAsObjectNode.remove("keyTags");
        Event e = mapper.readValue(eventAsObjectNode.traverse(), Event.class);

        JsonNode serialized = mapper.readTree(mapper.writeValueAsBytesUnchecked(e));
        JsonNode keyTagsPath = serialized.path("keyTags");
        assertTrue(MissingNode.getInstance().equals(keyTagsPath));
    }

    @Test(description = "Assert that the keytags key can be missing")
    public void keytagsCanBeMissing() throws Exception {
        eventAsObjectNode.remove("keyTags");
        Event e = mapper.readValue(eventAsObjectNode.traverse(), Event.class);
        assertEquals(e.getKeyTags(), ImmutableSet.of());
    }

    @Test(description = "Assert that we can serialize and deserialize and get an equals, but not same, object")
    public void toJsonAndBackAndStillEquals() throws Exception {
        Event original = mapper.readValue(eventAsObjectNode.traverse(), Event.class);
        String serialized = mapper.writeValueAsString(original);
        Event deserialized = mapper.readValue(serialized, Event.class);

        assertEquals(deserialized, original);
        assertNotSame(deserialized, original);
    }

    @Test(description = "Assert that keytags are added if present in the json payload")
    public void validateKeyTagsAsListOfKeyTags() throws Exception {
        Event event = mapper.readValue(eventAsObjectNode.traverse(), Event.class);

        ImmutableList<KeyTag> keytags = event.getKeyTags();

        assertEquals(keytags.size(), 1);
        assertEquals(keytags.get(0).getKey(), "P2V");
        assertEquals(keytags.get(0).getTags(), ImmutableSet.of("tag", "for", "only", "P2V", "readings"));

    }

    @Test(description = "Assert that event.ts does not change after serializing and deserializing again")
    public void tsValueDoesNotChange() throws Exception {
        Event event = mapper.readValue(validEventAsJsonString, Event.class);
        DateTime expected = event.getTimestamp();

        String serialized = mapper.writeValueAsString(event);
        Event deserialized = mapper.readValue(serialized, Event.class);

        assertEquals(deserialized.getTimestamp(), expected);
    }
}
