package io.sidecar.event;

import com.fasterxml.jackson.annotation.JsonProperty;

abstract class LocationMixin {

    @SuppressWarnings("unused")
    private LocationMixin(@JsonProperty("lat") double lat,
                          @JsonProperty("lon") double lon) {
    }
}

