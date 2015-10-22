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

public enum QueryType {

    LATEST("latest"),
    RANGE("range"),
    STATS("stats"),
    EXTENDED_STATS("extended_stats"),
    HISTOGRAM("histogram"),
    DATE_HISTOGRAM("date_histogram"),
    GEO_DISTANCE("geo_distance");

    private String name;

    private QueryType(String name) {
        this.name = name;
    }

    public static QueryType fromName(String shortName) {
        for (QueryType queryType : QueryType.values()) {
            if (queryType.name.equals(shortName)) {
                return queryType;
            }
        }
        throw new IllegalArgumentException("Illegal query type name: " + shortName);
    }

}
