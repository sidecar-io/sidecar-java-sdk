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

package io.sidecar.query;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.joda.time.DateTime;

import io.sidecar.geo.Location;

@SuppressWarnings("unused") //Used by Jackson
abstract class DataPointMixin {

    private DataPointMixin(@JsonProperty("ts") DateTime timestamp,
                           @JsonProperty("value") double value,
                           @JsonProperty("location") Location location) {
    }


    @SuppressWarnings("unused") //This is required to exist for Mixins during serialization
    @JsonGetter("ts")
    abstract String getTimestampAsIso8601String();

    @JsonIgnore
    abstract DateTime getTimestamp();

}
