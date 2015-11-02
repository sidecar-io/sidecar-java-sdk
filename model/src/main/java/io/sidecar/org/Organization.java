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

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;

import org.apache.commons.lang.StringUtils;

import java.util.Objects;
import java.util.UUID;

public class Organization {

    private UUID orgId;
    private String name;
    private String url;
    private OrganizationType type;

    public Organization(String name, String url, OrganizationType type) {
        this.orgId = UUID.randomUUID();

        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(StringUtils.isNotBlank(name));
        this.name = name;

        Preconditions.checkNotNull(url);
        Preconditions.checkArgument(StringUtils.isNotBlank(url));
        Preconditions.checkArgument(CharMatcher.WHITESPACE.matchesNoneOf(url));
        this.url = url;

        Preconditions.checkNotNull(type);
        this.type = type;
    }

    private Organization(Builder builder) {
        orgId = builder.orgId;
        name = builder.name;
        url = builder.url;
        type = builder.type;
    }

    public UUID getOrgId() {
        return orgId;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public OrganizationType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Organization{" +
               "orgId=" + orgId +
               ", name=" + name +
               ", url=" + url +
               ", type=" + type +
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

        Organization that = (Organization) o;

        return
              Objects.equals(this.orgId, that.orgId) &&
              Objects.equals(this.name, that.name) &&
              Objects.equals(this.url, that.url) &&
              Objects.equals(this.type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgId, name, url, type);
    }

    public static final class Builder {

        private UUID orgId;
        private String name;
        private String url;
        private OrganizationType type;

        public Builder() {
        }

        public Builder(Organization copy) {
            orgId = copy.orgId;
            name = copy.name;
            url = copy.url;
            type = copy.type;
        }

        public Builder orgId(UUID orgId) {
            this.orgId = orgId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder type(OrganizationType type) {
            this.type = type;
            return this;
        }

        public Organization build() {
            return new Organization(this);
        }
    }
}
