package io.sidecar.notification;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused") //Used By Jackson
public class NotificationRuleMixin {

    private NotificationRuleMixin(@JsonProperty("ruleId") UUID ruleId,
                                  @JsonProperty("name") String name,
                                  @JsonProperty("description") String description,
                                  @JsonProperty("appId") UUID appId,
                                  @JsonProperty("userId") UUID userId,
                                  @JsonProperty("stream") String stream,
                                  @JsonProperty("key") String key,
                                  @JsonProperty("min") double min,
                                  @JsonProperty("max") double max) {
    }
}
