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
