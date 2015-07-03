package io.sidecar.event;

import org.testng.annotations.Test;

import io.sidecar.geo.Location;
import io.sidecar.jackson.ModelMapper;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;

public class LocationJsonValidationTest {

    ModelMapper modelMapper = new ModelMapper();

    @Test(description =
          "Asserts that Location can be deserialized and serialized back in to an equals, but not "
          + "same, object")
    public void toJsonAndBack() throws Exception {
        Location original = new Location(19.0, -110.13434);
        String serialized = modelMapper.writeValueAsString(original);
        Location deserialized = modelMapper.readValue(serialized, Location.class);

        assertEquals(deserialized, original);
        assertNotSame(deserialized, original);
    }


}
