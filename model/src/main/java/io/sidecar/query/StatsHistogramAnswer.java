package io.sidecar.query;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class StatsHistogramAnswer implements Answer {

    private final ImmutableMap<String, Double> buckets;

    public StatsHistogramAnswer(Map<String, Double> statsKeys) {
        if (statsKeys == null) {
            buckets = ImmutableMap.of();
        } else {
            ImmutableMap.Builder<String, Double> imb = ImmutableMap.builder();
            for (Map.Entry<String, Double> e : statsKeys.entrySet()) {
                imb.put(e.getKey(), e.getValue());
            }
            buckets = imb.build();
        }
    }

    /**
     * Each stat is placed into an interval bucket
     *
     * @return the buckets and their respective numeric value
     */
    public ImmutableMap<String, Double> getBuckets() {
        return buckets;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        StatsHistogramAnswer that = (StatsHistogramAnswer) obj;

        return buckets.equals(that.buckets);
    }

    @Override
    public int hashCode() {
        int result = 31 * buckets.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "buckets{"
                + " buckets=" + buckets
                + '}';
    }
}
