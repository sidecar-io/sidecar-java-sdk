package io.sidecar.query;

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import io.sidecar.jackson.ModelMapper;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class StatsHistogramAnswerValidationTest {

    private String validStatsAnswerAsJsonString;
    private ModelMapper mapper = new ModelMapper();

    @BeforeMethod
    public void loadValidEventJsonAsString() throws Exception {
        validStatsAnswerAsJsonString =
                IOUtils.toString(this.getClass().getResource("/proposed_stats_histogram_answer.json"));
    }

    @Test
    public void statsHistogramAnswerCreatedFromJson() throws Exception {
        JsonNode arr = mapper.readTree(validStatsAnswerAsJsonString);
        StatsHistogramAnswer answerA = mapper.readValue(arr.get(0).traverse(), StatsHistogramAnswer.class);

        assertEquals(answerA.getBuckets().get("0"), Integer.valueOf(5));
        assertEquals(answerA.getBuckets().get("1"), Integer.valueOf(8));
    }

    @Test
    public void toJsonAndBackStillEquals() throws Exception {
        Map<String, Integer> bucketsAndValues = new HashMap<>();
        bucketsAndValues.put("0", 5);

        StatsHistogramAnswer original = new StatsHistogramAnswer(bucketsAndValues);
        String serialized = mapper.writeValueAsString(original);
        StatsHistogramAnswer deserialized = mapper.readValue(serialized, StatsHistogramAnswer.class);

        assertEquals(deserialized, original);
    }
}
