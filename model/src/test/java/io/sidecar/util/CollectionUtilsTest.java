package io.sidecar.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public class CollectionUtilsTest {

    @Test
    public void testFilterNullsList() {
        assertEquals(CollectionUtils.filterNulls(Lists.newArrayList("foo", null, "bar")),
                Lists.newArrayList("foo", "bar"));
    }

    @Test
    public void testFilterNullsSet() {
        assertEquals(CollectionUtils.filterNulls(Sets.newHashSet("foo", null, "bar")),
                Sets.newHashSet("foo", "bar"));
    }

    @Test
    public void testFilterNullsListParamIsNull() {
        assertEquals(CollectionUtils.filterNulls((List)null), ImmutableList.of());
    }

    @Test
    public void testFilterNullsSetParamIsNull() {
        assertEquals(CollectionUtils.filterNulls((Set)null), ImmutableSet.of());
    }

}