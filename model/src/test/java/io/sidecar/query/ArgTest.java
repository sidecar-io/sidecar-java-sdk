package io.sidecar.query;

import static org.testng.Assert.assertEquals;

import com.google.common.collect.ImmutableList;
import io.sidecar.ModelUtils;
import org.testng.annotations.Test;

public class ArgTest {

    @Test(description = "Asserts that a key can't contain spaces",
            expectedExceptions = IllegalArgumentException.class)
    public void keyCantContainSpaces() {
        new Arg("the key", "42");
    }

    @Test(description = "Asserts that a key can't contain new lines",
            expectedExceptions = IllegalArgumentException.class)
    public void keyCantContainNewLines() {
        new Arg("the\nkey", "42");
    }

    @Test(description = "Asserts that a key can't contain tabs",
            expectedExceptions = IllegalArgumentException.class)
    public void keyCantContainNewTabs() {
        new Arg("the\tkey", "42");
    }

    @Test(description = "Asserts that the value of a named arg key is correct")
    public void getArgKeyValue() {
        String result = ModelUtils.getArgKeyValue("foo", ImmutableList.<Arg>of(
                        new Arg("foo", "42"), new Arg("bar", "41"))
        );
        assertEquals(result, "42");
    }

    @Test(description = "Asserts that if a named key is not present it will return null")
    public void getMissingArgKeyValue() {
        String result = ModelUtils.getArgKeyValue("xyz", ImmutableList.<Arg>of(
                        new Arg("foo", "42"), new Arg("bar", "41"))
        );
        assertEquals(result, null);
    }

}

