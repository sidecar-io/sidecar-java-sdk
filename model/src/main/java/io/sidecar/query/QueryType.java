package io.sidecar.query;

public enum QueryType {

    LATEST("latest"),
    RANGE("range"),
    STATS("stats"),
    EXTENDED_STATS("extended_stats"),
    HISTOGRAM("histogram"),
    DATE_HISTOGRAM("date_histogram"),
    GEO_DISTANCE("geo_distance");

    private String name;

    private QueryType(String name) {
        this.name = name;
    }

    public static QueryType fromName(String shortName) {
        for (QueryType queryType : QueryType.values()) {
            if (queryType.name.equals(shortName)) {
                return queryType;
            }
        }
        throw new IllegalArgumentException("Illegal query type name: " + shortName);
    }

}
