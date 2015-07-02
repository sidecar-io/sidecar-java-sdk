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
