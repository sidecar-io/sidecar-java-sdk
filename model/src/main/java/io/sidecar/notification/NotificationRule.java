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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;
import java.util.UUID;

import com.google.common.base.Preconditions;
import io.sidecar.ModelUtils;
import io.sidecar.event.EventWithIds;
import org.apache.commons.lang.StringUtils;

/**
 * Model that encapsulates a rule that can be applied to an {@link EventWithIds} to determine if a single notification
 * needs to be generated due to the values in that Event.
 * <p>
 * Presently this is hardcoded only generate EventNotifications for a stream with the name "stream_id" and for values of key "11"
 * above 10350.  Eventually should be able to chain predicates together and perform more intelligent notification generation.
 */
public class NotificationRule {

    private final UUID ruleId;
    private final String name;
    private final String description;
    private final UUID appId;
    private final UUID userId;
    private final String stream;
    private final String key;
    private final double min;
    private final double max;

    public NotificationRule(UUID ruleId, String name, String description, UUID appId, UUID userId, String stream,
                            String key, double min, double max) {

        this.ruleId = checkNotNull(ruleId);

        checkArgument(StringUtils.isNotBlank(name) && name.length() <= 100);
        this.name = name.trim();

        checkArgument(StringUtils.isBlank(description) || description.length() <= 140);
        this.description = (description == null) ? "" : description.trim();

        this.appId = checkNotNull(appId);

        this.userId = checkNotNull(userId);

        Preconditions.checkArgument(ModelUtils.isValidStreamId(stream));
        this.stream = checkNotNull(stream);

        checkArgument(ModelUtils.isValidReadingKey(key));
        this.key = key;

        this.min = (min == Double.NEGATIVE_INFINITY || min == Double.NaN) ? Double.MIN_VALUE : min;
        this.max = (max == Double.POSITIVE_INFINITY || max == Double.NaN) ? Double.MAX_VALUE : max;
    }

    public UUID getRuleId() {
        return ruleId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getAppId() {
        return appId;
    }

    public String getStream() {
        return stream;
    }

    public String getKey() {
        return key;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationRule that = (NotificationRule) o;
        return Objects.equals(min, that.min) &&
                Objects.equals(max, that.max) &&
                Objects.equals(ruleId, that.ruleId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(appId, that.appId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(stream, that.stream) &&
                Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleId, name, appId, userId, stream, key, min, max);
    }

    @Override
    public String toString() {
        return "NotificationRule{" +
                "ruleId=" + ruleId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", appId=" + appId +
                ", userId=" + userId +
                ", stream='" + stream + '\'' +
                ", key='" + key + '\'' +
                ", min=" + min +
                ", max=" + max +
                '}';
    }

    public static class Builder {
        private UUID ruleId;
        private String name;
        private String description;
        private UUID appId;
        private UUID userId;
        private String stream;
        private String key;
        private double min = Double.NEGATIVE_INFINITY;
        private double max = Double.POSITIVE_INFINITY;

        public Builder ruleId(UUID ruleId) {
            this.ruleId = ruleId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
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

        public Builder stream(String stream) {
            this.stream = stream;
            return this;
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder min(double min) {
            this.min = min;
            return this;
        }

        public Builder max(double max) {
            this.max = max;
            return this;
        }

        public NotificationRule build() {
            return new NotificationRule(ruleId, name, description, appId, userId, stream, key, min, max);
        }
    }
}
