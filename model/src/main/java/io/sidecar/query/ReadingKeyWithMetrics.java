package io.sidecar.query;

import com.google.common.collect.ImmutableMap;

import org.apache.commons.lang.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class ReadingKeyWithMetrics {

    String readingKey;
    ImmutableMap<String, Double> metrics;

    public ReadingKeyWithMetrics(String readingKey, ImmutableMap<String, Double> metrics) {
        checkArgument(StringUtils.isNotBlank(readingKey));
        this.readingKey = readingKey;

        checkNotNull(metrics);
        this.metrics = metrics;
    }
}
