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
