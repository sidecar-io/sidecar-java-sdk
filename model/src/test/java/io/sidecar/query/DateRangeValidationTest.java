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

package io.sidecar.query;

import org.joda.time.DateTime;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class DateRangeValidationTest {

    @Test(description = "Asserts that a DateRange can be created with proper begin and end date")
    public void withValidBeginAndEnd() {
        DateTime begin = new DateTime().minusDays(1);
        DateTime end = new DateTime();

        DateRange dateRange = new DateRange(begin, end);
        assertEquals(dateRange.begin, begin);
        assertEquals(dateRange.end, end);
    }

    @Test(description = "Asserts that a DateRange can't be created with an end date before a begin date ",
          expectedExceptions = IllegalArgumentException.class)
    public void withEndBeforeBegin() {
        DateTime begin = new DateTime();
        DateTime end = new DateTime().minusDays(1);

        new DateRange(begin, end);
    }


}
