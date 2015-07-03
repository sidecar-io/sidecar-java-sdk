package io.sidecar.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

@SuppressWarnings("unused") //Used by Jackson
abstract public class StatsAnswerMixin {

    private StatsAnswerMixin(@JsonProperty("keys") Map<String, Map<String, Double>> keys) {

    }
}
