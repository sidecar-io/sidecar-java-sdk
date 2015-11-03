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

package io.sidecar.jackson;

import static org.testng.Assert.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.testng.annotations.Test;

import java.util.Objects;

public class ModelMapperTest {

    ModelMapper mapper = new ModelMapper();

    @Test
    public void testWriteThenReadAreEqual() {
        TestModel testModel = new TestModel("foo", 42.0);
        byte[] jsonAsBytes = mapper.writeValueAsBytesUnchecked(testModel);
        assertEquals(testModel, mapper.readValueUnchecked(jsonAsBytes, TestModel.class));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testReadInvalidString() {
        String invalid = "{\"text\":[],\"number\":42}";
        mapper.readValueUnchecked(invalid, TestModel.class);
    }

    private static class TestModel {
        @JsonProperty("text")
        String text;
        @JsonProperty("number")
        double number;

        private TestModel(){}

        public TestModel(String text, double number) {
            this.text = text;
            this.number = number;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestModel testModel = (TestModel) o;
            return Objects.equals(number, testModel.number) &&
                    Objects.equals(text, testModel.text);
        }

        @Override
        public int hashCode() {
            return Objects.hash(text, number);
        }
    }
}