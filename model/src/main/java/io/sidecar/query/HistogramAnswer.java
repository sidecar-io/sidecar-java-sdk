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

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class HistogramAnswer implements Answer {

    private final ImmutableMap<String, Integer> buckets;

    public HistogramAnswer(Map<String, Integer> b) {
        if (b == null) {
            buckets = ImmutableMap.of();
        } else {
            ImmutableMap.Builder<String, Integer> imb = ImmutableMap.builder();
            for (Map.Entry<String, Integer> e : b.entrySet()) {
                imb.put(e.getKey(), e.getValue());
            }
            buckets = imb.build();
        }
    }

    /**
     * Each stat is placed into an interval bucket
     *
     * @return the buckets and their respective numeric value
     */
    public ImmutableMap<String, Integer> getBuckets() {
        return buckets;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        HistogramAnswer that = (HistogramAnswer) obj;

        return buckets.equals(that.buckets);
    }

    @Override
    public int hashCode() {
        int result = 31 * buckets.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "buckets{"
                + " buckets=" + buckets
                + '}';
    }
}
