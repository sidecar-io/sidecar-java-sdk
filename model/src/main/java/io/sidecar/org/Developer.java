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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

/**
 * Model class that represents a Developer.
 */
public class Developer {

    private final UUID developerId;
    private final String username;
    private final String password;
    private final boolean active;

    private Developer(Builder b) {
        checkArgument(StringUtils.isNotBlank(b.username));
        this.username = b.username;

        checkArgument(StringUtils.isNotBlank(b.password));
        this.password = b.password;

        this.developerId = checkNotNull(b.developerId);
        this.active = b.active;
    }

    public UUID getDeveloperId() {
        return developerId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active;
    }


    public static class Builder {
        private UUID developerId;
        private String username;
        private String password;
        private boolean active;

        public Builder developerId(UUID developerId) {
            this.developerId = developerId;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Developer build() {
            return new Developer(this);
        }
    }
}
