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