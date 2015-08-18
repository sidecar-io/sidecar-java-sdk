package io.sidecar.query;

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import io.sidecar.jackson.ModelMapper;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HistogramAnswerValidationTest {

    private String validHistogramAsJsonString;
    private ModelMapper mapper = new ModelMapper();

    @BeforeMethod
    public void loadValidEventJsonAsString() throws Exception {
        validHistogramAsJsonString =
                IOUtils.toString(this.getClass().getResource("/proposed_histogram_answer.json"));
    }

    @Test
    public void histogramAnswerCreatedFromJson() throws Exception {
        JsonNode arr = mapper.readTree(validHistogramAsJsonString);
        HistogramAnswer answerA = mapper.readValue(arr.get(0).traverse(), HistogramAnswer.class);

        assertEquals(answerA.getBuckets().get("0"), Integer.valueOf(5));
        assertEquals(answerA.getBuckets().get("1"), Integer.valueOf(8));
    }

    @Test
    public void toJsonAndBackStillEquals() throws Exception {
        Map<String, Integer> bucketsAndValues = new HashMap<>();
        bucketsAndValues.put("0", 5);

        HistogramAnswer original = new HistogramAnswer(bucketsAndValues);
        String serialized = mapper.writeValueAsString(original);
        HistogramAnswer deserialized = mapper.readValue(serialized, HistogramAnswer.class);

        assertEquals(deserialized, original);
    }
}
