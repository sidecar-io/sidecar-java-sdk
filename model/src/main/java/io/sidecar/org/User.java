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

package io.sidecar.org;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.UUID;

public final class User {

    private final UUID userId;
    private final String username;
    private ImmutableMap<String,String> metadata;

    private User(Builder builder) {
        userId = builder.userId;
        username = builder.username;
        metadata = (builder.metadata == null) ? ImmutableMap.<String,String>of() : ImmutableMap.copyOf(builder.metadata);
    }

    public String getUsername() {
        return username;
    }

    public UUID getUserId() {
        return userId;
    }

    public ImmutableMap<String,String> getMetadata() {
        return metadata;
    }


    public static final class Builder {

        private UUID userId = UUID.randomUUID();
        private String username;
        private Map<String,String> metadata;

        public Builder() {
        }

        /**
         * Initializes a Builder from a previously existing User reference.  This constructor should be used to copy
         * and edit a User if modifications should be made to an existing user.
         *
         * @param original An existing User reference used to initialize all of the Builder fields.
         */
        public Builder(User original) {
            userId = original.userId;
            username = original.username;
            metadata = original.metadata;
        }

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder metadata(Map<String,String> metadata) {
            this.metadata = metadata;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
