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

import java.util.UUID;

/**
 * Model that represents a view of a user belonging to a group. Encapsulates a user and their role
 * in the group.
 */
public class UserGroupMember {

    private final UserGroupRole role;
    private final UUID userId;

    public UserGroupMember(UUID userId, UserGroupRole role) {
        this.role = role;
        this.userId = userId;
    }

    public UserGroupRole getRole() {
        return role;
    }

    public UUID getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserGroupMember that = (UserGroupMember) o;

        if (role != that.role) {
            return false;
        }
        if (!userId.equals(that.userId)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = role.hashCode();
        result = 31 * result + userId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserGroupMember{" +
               "role=" + role +
               ", userId=" + userId +
               '}';
    }
}
