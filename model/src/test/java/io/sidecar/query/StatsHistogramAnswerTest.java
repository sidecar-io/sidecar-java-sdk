package io.sidecar.query;

import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

public class StatsHistogramAnswerTest {

    @Test
    public void testStatsHistogramAnswer() {

        Map<String, Integer> bucketsAndValues = new HashMap<>();
        bucketsAndValues.put("0", 42);

        StatsHistogramAnswer answer = new StatsHistogramAnswer(bucketsAndValues);

        assertTrue(answer.getBuckets().containsKey("0"));
    }

}
