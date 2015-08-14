package io.sidecar.query;

import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

public class StatsHistogramAnswerTest {

    @Test
    public void testStatsHistogramAnswer() {

        Map<String, Double> bucketsAndValues = new HashMap<>();
        bucketsAndValues.put("0", 42.0);

        StatsHistogramAnswer answer = new StatsHistogramAnswer(bucketsAndValues);

        assertTrue(answer.getBuckets().containsKey("0"));
    }

}
