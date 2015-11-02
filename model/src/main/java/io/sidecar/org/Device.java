/*
 * Copyright 2015 QSense, Inc.
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

package io.sidecar.org;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.UUID;

public class Device {

    private UUID userId;
    private UUID appId;
    private UUID deviceId;
    private ImmutableMap<String,String> metadata;

    private Device(Builder builder) {
        userId = builder.userId;
        deviceId = builder.deviceId;
        appId = builder.appId;
        metadata =  (builder.metadata == null) ? ImmutableMap.<String,String>of() : ImmutableMap.copyOf(builder.metadata);
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public UUID getAppId() {
        return appId;
    }

    public ImmutableMap<String,String> getMetadata() {
        return metadata;
    }

    public static final class Builder {

        private UUID userId;
        private UUID deviceId;
        private UUID appId;
        private Map<String,String> metadata;

        public Builder() {
        }

        public Builder(Device copy) {
            userId = copy.userId;
            deviceId = copy.deviceId;
            appId = copy.appId;
            metadata = copy.metadata;
        }

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder deviceId(UUID deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder appId(UUID appId) {
            this.appId = appId;
            return this;
        }

        public Builder metadata(Map<String,String> metadata) {
            this.metadata = metadata;
            return this;
        }

        public Device build() {
            return new Device(this);
        }
    }
}
