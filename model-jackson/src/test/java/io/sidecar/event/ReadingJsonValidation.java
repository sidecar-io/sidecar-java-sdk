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
