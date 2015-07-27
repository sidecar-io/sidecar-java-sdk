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
