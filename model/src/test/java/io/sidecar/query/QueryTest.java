package io.sidecar.query;

import org.testng.annotations.Test;

import static java.util.Collections.singletonList;

public class QueryTest {

    @Test(description = "Assert than an Query stream can't contain spaces",
          expectedExceptions = IllegalArgumentException.class)
    public void streamCantContainSpaces() {
        new Query("STREAM NAME", singletonList(new Arg("somekey", "somevalue")));
    }

    @Test(description = "Assert that stream name can be not be null",
          expectedExceptions = IllegalArgumentException.class)
    public void nulStreamArgumentIsInvalid() {
        new Query(null, singletonList(new Arg("somekey", "somevalue")));
    }


}
