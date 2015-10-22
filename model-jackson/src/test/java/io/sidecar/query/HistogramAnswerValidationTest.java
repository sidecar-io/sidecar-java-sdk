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
