package io.sidecar.event;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

import static io.sidecar.ModelTestUtils.createSampleEvent;

public class EventWithIdsTest {

    @Test(description = "Asserts that the params that are part of an EventWithIds is lowercased and valid")
    public void initWithValidFields() {
        UUID org = UUID.randomUUID();
        UUID app = UUID.randomUUID();
        UUID user = UUID.randomUUID();
        EventWithIds eventWithIds = new EventWithIds.Builder()
              .event(createSampleEvent()).orgId(org).appId(app).userId(user).build();
        Assert.assertEquals(eventWithIds.getOrgId(), org);
        Assert.assertEquals(eventWithIds.getAppId(), app);
        Assert.assertEquals(eventWithIds.getUserId(), user);

    }
}
