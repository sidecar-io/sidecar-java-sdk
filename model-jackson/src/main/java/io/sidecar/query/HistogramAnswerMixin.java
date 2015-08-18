package io.sidecar.query;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused") //Used by Jackson
abstract public class HistogramAnswerMixin {

    private HistogramAnswerMixin(@JsonProperty("buckets") Map<String, Integer> buckets) {

    }
}
