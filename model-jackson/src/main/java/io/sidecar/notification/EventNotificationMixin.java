package io.sidecar.notification;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

@SuppressWarnings("unused") //Used by Jackson
abstract class EventNotificationMixin {

    private EventNotificationMixin(@JsonProperty("rule") NotificationRule rule,
                                   @JsonProperty("orgId") UUID orgId,
                                   @JsonProperty("appId") UUID appId,
                                   @JsonProperty("userId") UUID userId,
                                   @JsonProperty("ts") DateTime timestamp) {}

    @JsonGetter("ts")
    abstract DateTime getTimestamp();
}
