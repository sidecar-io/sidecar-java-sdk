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
