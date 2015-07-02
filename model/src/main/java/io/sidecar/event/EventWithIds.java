package io.sidecar.event;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;
import java.util.UUID;

public class EventWithIds  {

    private final Event event;
    private final UUID appId;
    private final UUID userId;
    private final UUID orgId;


    @SuppressWarnings("unused")
    private EventWithIds(Event event, UUID orgId, UUID appId, UUID userId) {
        checkNotNull(event);
        checkNotNull(orgId);
        checkNotNull(appId);
        checkNotNull(userId);

        this.event = event;
        this.appId = appId;
        this.orgId = orgId;
        this.userId = userId;
    }

    private EventWithIds(Builder b) {
        checkNotNull(b.event);
        checkNotNull(b.orgId);
        checkNotNull(b.appId);
        checkNotNull(b.userId);

        this.event = b.event;
        this.appId = b.appId;
        this.orgId = b.orgId;
        this.userId = b.userId;
    }

    public Event getEvent() {
        return event;
    }

    public UUID getAppId() {
        return appId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getOrgId() {
        return orgId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final EventWithIds that = (EventWithIds) o;

        return Objects.equals(this.event, that.event)
                && Objects.equals(this.orgId, that.orgId)
                && Objects.equals(this.userId, that.userId)
                && Objects.equals(this.appId, that.appId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, orgId, appId, userId);
    }

    @Override
    public String toString() {
        return "EventWithIds{" +
                "event=" + event +
                ", orgId='" + orgId + '\'' +
                ", appId='" + appId + '\'' +
                ", userId='" + userId + '\'' +
                "} " + super.toString();
    }


    public static class Builder {

        private Event event;
        private UUID appId;
        private UUID userId;
        private UUID orgId;

        public Builder() {
        }

        public Builder(EventWithIds eventWithIds) {
            this.event = eventWithIds.event;
            this.appId = eventWithIds.appId;
            this.userId = eventWithIds.userId;
            this.orgId = eventWithIds.orgId;
        }

        public Builder event(Event event) {
            this.event = event;
            return this;
        }

        public Builder appId(UUID appId) {
            this.appId = appId;
            return this;
        }

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder orgId(UUID orgId) {
            this.orgId = orgId;
            return this;
        }

        public EventWithIds build() {
            return new EventWithIds(this);
        }
    }
}
