package io.sidecar.query;

import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

public class HistogramAnswerTest {

    @Test
    public void testHistogramAnswer() {

        Map<String, Integer> bucketsAndValues = new HashMap<>();
        bucketsAndValues.put("0", 42);

        HistogramAnswer answer = new HistogramAnswer(bucketsAndValues);

        assertTrue(answer.getBuckets().containsKey("0"));
    }

}
