package io.sidecar.query;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;


public final class StatsAnswer implements Answer {

    private final ImmutableMap<String, ImmutableMap<String, Double>> keys;

    public StatsAnswer(Map<String, Map<String, Double>> statsKeys) {
        if (statsKeys == null) {
            keys = ImmutableMap.of();
        } else {
            Builder<String, ImmutableMap<String, Double>> imb = ImmutableMap.builder();
            for (Map.Entry<String, Map<String, Double>> e : statsKeys.entrySet()) {
                imb.put(e.getKey(), ImmutableMap.copyOf(e.getValue()));
            }
            keys = imb.build();
        }
    }

    /**
     * Each stat calculated is assigned to a key value pair (ie mean : 10.01)
     *
     * @return the stat keys and their respective numeric value
     */
    public ImmutableMap<String, ImmutableMap<String, Double>> getKeys() {
        return keys;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        StatsAnswer that = (StatsAnswer) obj;

        return keys.equals(that.keys);
    }

    @Override
    public int hashCode() {
        int result = 31 * keys.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "StatsAnswer{"
                + " keys=" + keys
                + '}';
    }
}