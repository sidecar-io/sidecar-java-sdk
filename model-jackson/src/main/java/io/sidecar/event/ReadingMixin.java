/*
 * Copyright 2015 QSense, Inc.
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

package io.sidecar.event;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Optional;
import org.joda.time.DateTime;


@SuppressWarnings("unused") //Used by Jackson
abstract class ReadingMixin {

    private ReadingMixin(@JsonProperty("key") String key,
                         @JsonProperty("ts") DateTime timestamp,
                         @JsonProperty("value") double value) {
    }

    @JsonIgnore
    abstract Optional<DateTime> getTimestamp();

    @JsonGetter("ts")
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    abstract String getTimestampAsIso8601String();
}
