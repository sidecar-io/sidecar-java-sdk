/*
 * Copyright 2015 Sidecar.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.sidecar.notification;

import org.joda.time.DateTime;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.sidecar.util.DateUtils.nowUtc;
import static org.joda.time.DateTimeZone.UTC;

import java.util.Objects;
import java.util.UUID;

public class EventNotification {

    private final UUID id;
    private final NotificationRule rule;
    private final UUID appId;
    private final UUID userId;
    private final UUID orgId;
    private final DateTime timestamp;


    @SuppressWarnings("unused")
    private EventNotification(NotificationRule rule, UUID orgId, UUID appId, UUID userId, DateTime timestamp) {
        this.id = UUID.randomUUID();
        this.rule = checkNotNull(rule);
        this.orgId = checkNotNull(orgId);
        this.appId = checkNotNull(appId);
        this.userId = checkNotNull(userId);
        this.timestamp = timestamp != null ? timestamp.withZone(UTC): nowUtc();
    }

    private EventNotification(Builder b) {
        this(b.rule, b.orgId, b.appId, b.userId, b.timestamp);
    }

    public NotificationRule getRule() {
        return rule;
    }

    public UUID getId() {
        return id;
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

    /**
     * @return A DateTime representing the instant this EventNotification was generated in UTC.
     */
    public DateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final EventNotification that = (EventNotification) o;

        return  Objects.equals(this.id, that.id)
                && Objects.equals(this.orgId, that.orgId)
                && Objects.equals(this.userId, that.userId)
                && Objects.equals(this.appId, that.appId)
                && Objects.equals(this.rule, that.getRule())
                && Objects.equals(this.timestamp, that.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgId, userId, appId, rule, timestamp);
    }

    @Override
    public String toString() {
        return "EventNotification{" +
                "id=" + id +
                ", orgId='" + orgId + '\'' +
                ", appId='" + appId + '\'' +
                ", userId='" + userId + '\'' +
                ", rule='" + rule + '\'' +
                ", timestamp='" + timestamp + '\'' +
                "} " + super.toString();
    }

    public static class Builder {

        private NotificationRule rule;
        private UUID appId;
        private UUID userId;
        private UUID orgId;
        private DateTime timestamp;


        @SuppressWarnings("unused")
        public Builder notificationRule(NotificationRule rule) {
            this.rule = rule;
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

        /**
         * Sets the time this EventNotification was generated.  If not called or if null is passed in, calling build()
         * will use a timestamp of the instant that build() was called.
         *
         * @param timestamp the timestamp of this EventNotification, if different than current system time.
         * @return a reference to this Builder
         */
        public Builder timestamp(DateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }


        public EventNotification build() {
            return new EventNotification(this);
        }
    }


}
