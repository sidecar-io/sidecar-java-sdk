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

import com.google.common.collect.ImmutableList;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class UserGroup {

    private final UUID id;
    private final UUID appId;
    private final String name;
    private final ImmutableMap<String, String> metadata;
    private final ImmutableList<UserGroupMember> members;
    private final ImmutableList<UUID> deviceIds;

    private UserGroup(UUID id, UUID appId, String name, Map<String, String> metadata,
                      List<UserGroupMember> members, List<UUID> deviceIds) {
        checkNotNull(id, "UserGroup must have a non-null id");
        this.id = id;

        checkNotNull(appId, "UserGroup must have a non-null appId");
        this.appId = appId;

        checkArgument(StringUtils.isNotBlank(name) && name.length() <= 100,
                "UserGroup name must be non-blank and less than or equal to 100 characters in length.");
        this.name = name;

        this.metadata = (metadata == null) ? ImmutableMap.<String,String>of() : ImmutableMap.copyOf(metadata);
        this.members = (members == null) ? ImmutableList.<UserGroupMember>of() : ImmutableList.copyOf(members);
        this.deviceIds = (deviceIds == null) ? ImmutableList.<UUID>of() : ImmutableList.copyOf(deviceIds);
    }

    private UserGroup(Builder b) {
        this(b.id, b.appId, b.name, b.metadata, b.members, b.deviceIds);
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public UUID getAppId() {
        return appId;
    }

    public ImmutableMap<String, String> getMetadata() {
        return metadata;
    }

    public ImmutableList<UserGroupMember> getMembers() {
        return members;
    }

    public ImmutableList<UUID> getDeviceIds() {
        return deviceIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserGroup userGroup = (UserGroup) o;

        if (!appId.equals(userGroup.appId)) {
            return false;
        }
        if (!deviceIds.equals(userGroup.deviceIds)) {
            return false;
        }
        if (!id.equals(userGroup.id)) {
            return false;
        }
        if (!members.equals(userGroup.members)) {
            return false;
        }
        if (!metadata.equals(userGroup.metadata)) {
            return false;
        }
        if (!name.equals(userGroup.name)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + appId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + metadata.hashCode();
        result = 31 * result + members.hashCode();
        result = 31 * result + deviceIds.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserGroup{"
                + "id=" + id
                + ", appId=" + appId
                + ", name='" + name + '\''
                + ", metadata=" + metadata
                + ", members=" + members
                + ", deviceIds=" + deviceIds
                + '}';
    }

    public static class Builder {

        UUID id;
        UUID appId;
        String name;
        Map<String,String> metadata = new HashMap<>();
        List<UserGroupMember> members = new ArrayList<>();
        List<UUID> deviceIds = new ArrayList<>();

        public UserGroup build() {
            return new UserGroup(this);
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public UUID getId() {
            return id;
        }

        public Builder name(String name) {
            this.name = name;
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

        public Builder members(List<UserGroupMember> members) {
            this.members = members;
            return this;
        }

        public Builder appendMember(UserGroupMember member) {
            if (member != null) {
                members.add(member);
            }
            return this;
        }

        public Builder deviceIds(List<UUID> deviceIds) {
            this.deviceIds = deviceIds;
            return this;
        }

        public Builder appendDeviceId(UUID deviceId) {
            if (deviceId != null) {
                deviceIds.add(deviceId);
            }
            return this;
        }

    }
}
