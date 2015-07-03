package io.sidecar.query;

import com.google.common.collect.ImmutableMap;

import com.fasterxml.jackson.annotation.JsonProperty;


@SuppressWarnings("unused") //Used by Jackson
public class ReadingKeyWithMetricsMixin {

    private ReadingKeyWithMetricsMixin(@JsonProperty("") String readingKey,
                                       ImmutableMap<String, Double> metrics) {

    }
}


