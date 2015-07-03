package io.sidecar.org;

import org.testng.annotations.Test;

import java.util.UUID;

import io.sidecar.jackson.ModelMapper;

import static org.testng.Assert.assertEquals;

public class UserGroupJsonValidationTest {

    private ModelMapper mapper = new ModelMapper();

    @Test(description =
          "Assert that serializing to json and deserializing back again produces an equal " +
          "UserGroup object")
    public void toAndFromJsonProducesEqual() throws Exception {
        UserGroup original = new UserGroup.Builder()
              .id(UUID.randomUUID())
              .appendMember(new UserGroupMember(UUID.randomUUID(), UserGroupRole.ADMIN))
              .appendDeviceId(UUID.randomUUID())
              .appId(UUID.randomUUID())
              .name("Test Group")
              .build();

        String asJson = mapper.writeValueAsString(original);

        UserGroup deserialized = mapper.readValue(asJson, UserGroup.class);
        assertEquals(deserialized, original);
    }


}
