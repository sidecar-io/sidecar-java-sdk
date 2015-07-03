package io.sidecar.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class EventWithIdsMixin {

    @SuppressWarnings("unused")
    private EventWithIdsMixin(@JsonProperty("event") Event event,
                              @JsonProperty("orgId") UUID orgId,
                              @JsonProperty("appId") UUID appId,
                              @JsonProperty("userId") UUID userId) {
    }
}
