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

import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

public class StatsAnswerTest {

    @Test
    public void testStatsAnswer() {

        Map<String, Double> metricsAndValues = new HashMap<>();
        metricsAndValues.put("METRIC_NAME", 42.0);

        Map<String, Map<String, Double>> keysAndMetrics = new HashMap<>();
        keysAndMetrics.put("DV1", metricsAndValues);

        StatsAnswer answer = new StatsAnswer(keysAndMetrics);

        assertTrue(answer.getKeys().containsKey("DV1"));
    }

}
