package io.sidecar.event;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.UUID;

import io.sidecar.geo.Location;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.testng.Assert.assertEquals;

public class EventTest {

    private Event.Builder createBuilderWithExpectedValues() {
        return new Event.Builder()
                .id(randomUUID())
                .timestamp(DateTime.now(DateTimeZone.UTC))
                .deviceId(UUID.randomUUID())
                .stream("streamname")
                .tags(Sets.newHashSet("foo", "bar"))
                .location(new Location(0.0, 0.0))
                .readings(singletonList(new Reading("key", DateTime.now(DateTimeZone.UTC), 1L)))
                .keyTags(new ImmutableMap.Builder<String, Collection<String>>()
                        .put("key", Sets.newHashSet("keytag"))
                        .build());
    }

    @Test(description = "Assert that an Event created with expected values passes validation on construction")
    public void validEventBaseCase() {
        createBuilderWithExpectedValues().build();
    }

    @Test(description = "Assert that the passed in set of tags can be null")
    public void nullTagsArgumentIsValid() {
        createBuilderWithExpectedValues()
                .tags(null)
                .build();
    }

    @Test(description = "Assert that an Event's stream id is lowercased")
    public void initWithUpperCaseStream() {
        Event e = createBuilderWithExpectedValues().stream("STREAMNAME").build();
        assertEquals(e.getStream(), "streamname");
    }

    @Test(description = "Assert than an Event stream can't contain new lines",
            expectedExceptions = IllegalArgumentException.class)
    public void streamCantContainLineBreaks() {
        createBuilderWithExpectedValues().stream("STREAMNAME\n").build();
    }

    @Test(description = "Assert than an Event stream can't contain tabs",
            expectedExceptions = IllegalArgumentException.class)
    public void streamCantContainTabs() {
        createBuilderWithExpectedValues().stream("STREAM\tNAME").build();
    }

    @Test(description = "Assert than an Event stream can't contain spaces",
            expectedExceptions = IllegalArgumentException.class)
    public void streamCantContainSpaces() {
        createBuilderWithExpectedValues().stream("STREAM NAME").build();
    }

    @Test(description = "Assert than a single tag cant contain spaces if it has other characters",
            expectedExceptions = IllegalArgumentException.class)
    public void singleTagCantContainSpaces() {
        createBuilderWithExpectedValues().tags(newArrayList("foo", "bar baz")).build();
    }

    @Test(description = "Assert than a single tag cant contain a new line if it has other characters",
            expectedExceptions = IllegalArgumentException.class)
    public void singleTagCantContainNewLines() {
        createBuilderWithExpectedValues().tags(newArrayList("foo", "bar\nbaz")).build();
    }

    @Test(description = "Assert than a single tag cant contain tabs it has other characters",
            expectedExceptions = IllegalArgumentException.class)
    public void singleTagCantContainTabs() {
        createBuilderWithExpectedValues().tags(newArrayList("foo", "bar\tbaz")).build();
    }

    @Test(description = "Assert than a single keytag cant contain spaces if it has other characters",
            expectedExceptions = IllegalArgumentException.class)
    public void singleKeyTagCantContainSpaces() {
        createBuilderWithExpectedValues()
                .keyTags(new ImmutableMap.Builder<String, Collection<String>>()
                        .put("key", Sets.newHashSet("key tag")).build())
                .build();

    }

    @Test(description = "Assert than a single keytag cant contain a new line if it has other characters",
            expectedExceptions = IllegalArgumentException.class)
    public void singleKeyTagCantContainNewLines() {
        createBuilderWithExpectedValues()
                .keyTags(new ImmutableMap.Builder<String, Collection<String>>()
                        .put("key", Sets.newHashSet("key\ntag")).build())
                .build();
    }

    @Test(description = "Assert than a single keytag can't contain tabs if it has other characters",
            expectedExceptions = IllegalArgumentException.class)
    public void singleKeyTagCantContainTabs() {
        createBuilderWithExpectedValues()
                .keyTags(new ImmutableMap.Builder<String, Collection<String>>()
                        .put("key", Sets.newHashSet("key\ttag")).build())
                .build();
    }

    @Test(description = "A key tag can't be over 40 characters", expectedExceptions = IllegalArgumentException.class)
    public void singleKeyTagCantHaveOver40Chars() {
        createBuilderWithExpectedValues()
                .keyTags(new ImmutableMap.Builder<String, Collection<String>>()
                        .put(RandomStringUtils.randomAlphabetic(41), Sets.newHashSet("foo")).build())
                .build();
    }


}
