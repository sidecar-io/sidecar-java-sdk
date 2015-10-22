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

package io.sidecar.notification;



import org.apache.commons.lang.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.UUID;

public class NotificationRuleTest {

    NotificationRule.Builder completeBuilder = new NotificationRule.Builder()
            .appId(UUID.randomUUID())
            .description("test description")
            .key(RandomStringUtils.randomAlphabetic(40))
            .max(100)
            .min(0)
            .name("sample rule name")
            .ruleId(UUID.randomUUID())
            .stream("my_stream")
            .userId(UUID.randomUUID());

    @Test(description = "Happy path test with no invalid fields")
    public void happyPath() {
        completeBuilder.build();
    }

    @Test(description = "Notification rule key can't be over 40 characters",
            expectedExceptions = IllegalArgumentException.class)
    public void keyCantExceed40Chars() {
        completeBuilder.key(RandomStringUtils.randomAlphabetic(41)).build();
    }
}