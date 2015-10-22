/*
 * Copyright 2015 Sidecar.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
