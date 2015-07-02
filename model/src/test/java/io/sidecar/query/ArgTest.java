package io.sidecar.query;

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

}

