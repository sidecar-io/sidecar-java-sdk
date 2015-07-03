package io.sidecar.access;

import org.testng.annotations.Test;

import io.sidecar.jackson.ModelMapper;

import static org.testng.Assert.assertEquals;

public class AccessKeyJsonValidationTest {

    ModelMapper mapper = new ModelMapper();

    @Test(description = "Asserts that serializing an AccessKey to json and then deserializing it again produces an "
          + "equal object")
    public void serializeAndDeserialize() throws Exception {
        AccessKey original = new AccessKey.Builder()
              .keyId("SFOFWRSON72LTDXS0G8H")
              .secret("hkajslhdflkjashflkajfhaf")
              .build();

        String json = mapper.writeValueAsString(original);
        AccessKey deserialized = mapper.readValue(json, AccessKey.class);

        assertEquals(original, deserialized);
    }
}
