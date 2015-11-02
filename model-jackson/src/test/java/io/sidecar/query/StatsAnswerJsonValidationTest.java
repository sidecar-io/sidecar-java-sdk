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

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import io.sidecar.jackson.ModelMapper;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class StatsAnswerJsonValidationTest {

    private String validStatsAnswerAsJsonString;
    private ModelMapper mapper = new ModelMapper();

    @BeforeMethod
    public void loadValidEventJsonAsString() throws Exception {
        validStatsAnswerAsJsonString =
              IOUtils.toString(this.getClass().getResource("/proposed_stats_answer.json"));
    }

    @Test
    public void statsAnswerCreatedFromJson() throws Exception {
        JsonNode arr = mapper.readTree(validStatsAnswerAsJsonString);
        StatsAnswer answerA = mapper.readValue(arr.get(0).traverse(), StatsAnswer.class);
        StatsAnswer answerB = mapper.readValue(arr.get(1).traverse(), StatsAnswer.class);

        assertEquals(answerA.getKeys().get("DV1").get("avg"), 42.0);
        assertEquals(answerA.getKeys().get("DV2").get("max"), 84.84);

        assertEquals(answerB.getKeys().get("DV1").get("avg"), 100.0);
        assertEquals(answerB.getKeys().get("DV2").get("max"), 1000.0);
    }

    @Test
    public void toJsonAndBackStillEquals() throws Exception {
        Map<String, Double> metricsAndValues = new HashMap<>();
        metricsAndValues.put("METRIC_NAME", 42.0);

        Map<String, Map<String, Double>>  keysAndMetrics = new HashMap<>();
        keysAndMetrics.put("DV1", metricsAndValues);

        StatsAnswer original = new StatsAnswer(keysAndMetrics);
        String serialized = mapper.writeValueAsString(original);
        StatsAnswer deserialized = mapper.readValue(serialized, StatsAnswer.class);

        assertEquals(deserialized, original);
    }
}
