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
