package io.sidecar.query;

import org.testng.annotations.Test;

import io.sidecar.jackson.ModelMapper;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;

public class ArgJsonValidationTest {

    @Test(description =
          "Asserts that Args can be deserialized and serialized back in to an equals, but not " +
          "same, object")
    public void toJsonAndBack() throws Exception {
        ModelMapper mapper = ModelMapper.instance();
        Arg original = new Arg("ABC", "123");
        String serialized = mapper .writeValueAsString(original);
        Arg deserialized = mapper.readValue(serialized, Arg.class);

        assertNotSame(deserialized, original);
        assertEquals(deserialized, original);
    }
}
