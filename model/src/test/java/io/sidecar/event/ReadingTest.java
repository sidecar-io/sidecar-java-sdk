/*
 * Copyright 2015 QSense, Inc.
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

import static io.sidecar.util.DateUtils.nowUtc;
import static org.testng.Assert.assertEquals;

import com.google.common.base.Optional;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.testng.annotations.Test;


public class ReadingTest {

    @Test(description = "Asserts that a key can't contain spaces",
          expectedExceptions = IllegalArgumentException.class)
    public void keyCantContainSpaces() {
        new Reading("the key", nowUtc(), 42);
    }

    @Test(description = "Asserts that a key can't contain new lines",
          expectedExceptions = IllegalArgumentException.class)
    public void keyCantContainNewLines() {
        new Reading("the\nkey", nowUtc(), 42);
    }

    @Test(description = "Asserts that a key can't contain tabs",
          expectedExceptions = IllegalArgumentException.class)
    public void keyCantContainNewTabs() {
        new Reading("the\tkey", nowUtc(), 42);
    }

    @Test(description = "Reading timestamp is optional")
    public void timestampIsOptional() {
        assertEquals(new Reading("key",42).getTimestamp(), Optional.absent());
    }

    @Test(description = "Key can't be over 40 characters", expectedExceptions = IllegalArgumentException.class)
    public void keyCantExceed40Chars() {
        new Reading(RandomStringUtils.randomAlphabetic(41), nowUtc(), 0.99);
    }

    @Test(description = "Key can be exactly 40 characters")
    public void keyMaxSize() {
        new Reading(RandomStringUtils.randomAlphabetic(40), nowUtc(), 0.99);
    }

}
