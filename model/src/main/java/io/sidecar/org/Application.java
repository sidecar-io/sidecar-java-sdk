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

import java.util.Objects;
import java.util.UUID;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;

public class Application {

    private UUID orgId;
    private UUID appId;
    private String description;
    private String name;

    public Application(UUID orgId, String name, String description) {
        Preconditions.checkNotNull(orgId);
        this.orgId = orgId;

        // assign the AppId
        this.appId = UUID.randomUUID();

        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(StringUtils.isNotBlank(name));
        this.name = name;

        Preconditions.checkNotNull(description);
        Preconditions.checkArgument(StringUtils.isNotBlank(description));
        this.description = description;
    }

    private Application(Builder builder) {
        appId = builder.appId;
        name = builder.name;
        orgId = builder.orgId;
        description = builder.description;
    }

    public UUID getAppId() {
        return appId;
    }

    public UUID getOrgId() {
        return orgId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Application{" +
                "orgId=" + orgId +
                ", appId=" + appId +
                ", name=" + name +
                ", description=" + description +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Application that = (Application) o;

        return
                Objects.equals(this.orgId, that.orgId) &&
                        Objects.equals(this.appId, that.appId) &&
                        Objects.equals(this.description, that.description) &&
                        Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgId, appId, name, description);
    }

    public static final class Builder {

        private UUID appId;
        private String name;
        private UUID orgId;
        private String description;

        public Builder() {
        }

        public Builder(Application copy) {
            appId = copy.appId;
            name = copy.name;
            orgId = copy.orgId;
            description = copy.description;
        }

        public Builder appId(UUID appId) {
            this.appId = appId;
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

        public Builder orgId(UUID orgId) {
            this.orgId = orgId;
            return this;
        }

        public Application build() {
            return new Application(this);
        }
    }
}
