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

package io.sidecar.access;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * Access info contains non-sensitive user information, mostly likely used in the REST resource. For
 * example, to inject the appId into the payload or to get a uses keyTypes. It should not contain
 * any security related fields.
 */
public final class AccessInfo {

    protected final UUID orgId;
    protected final UUID appId;
    protected final UUID userId;
    protected final AccessKeyType keyType;

    public AccessInfo(UUID orgId, UUID appId, UUID userId, AccessKeyType keyType) {
        this.orgId = checkNotNull(orgId);
        this.appId = checkNotNull(appId);
        this.userId = userId;
        this.keyType = checkNotNull(keyType);
    }

    private AccessInfo(Builder b) {
        this(b.orgId, b.appId, b.userId, b.keyType);
    }

    /**
     * @return A non-null id for the Application.
     */
    public UUID getAppId() {
        return appId;
    }

    /**
     * @return A non-null id for the Organization.
     */
    public UUID getOrgId() {
        return orgId;
    }

    /**
     * @return An id for the User, or null if this instance represents a non-user entity.
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * @return The type of entity this AccessInfo instance represents.
     */
    public AccessKeyType getKeyType() {
        return keyType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessInfo that = (AccessInfo) o;
        return Objects.equals(orgId, that.orgId) &&
                Objects.equals(appId, that.appId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(keyType, that.keyType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgId, appId, userId, keyType);
    }

    public static final class Builder {

        private UUID orgId;
        private UUID appId;
        private UUID userId;
        private AccessKeyType keyType;

        public Builder() {
        }

        public Builder(AccessInfo copy) {
            orgId = copy.orgId;
            appId = copy.appId;
            userId = copy.userId;
            keyType = copy.keyType;
        }

        public Builder orgId(UUID orgId) {
            this.orgId = orgId;
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

        public Builder keyType(AccessKeyType keyType) {
            this.keyType = keyType;
            return this;
        }

        public AccessInfo build() {
            return new AccessInfo(this);
        }
    }
}
