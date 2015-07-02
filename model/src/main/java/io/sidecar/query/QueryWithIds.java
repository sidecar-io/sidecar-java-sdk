package io.sidecar.query;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableList;

public class QueryWithIds {

    private final Query query;
    private final UUID orgId;
    private final UUID appId;
    private final UUID userId; // initially from accessInfo
    private final List<UUID> members; // from a getUsersInGroup call.
    private final UUID deviceId;

    private QueryWithIds(Builder builder) {
        query = builder.query;
        deviceId = builder.deviceId;
        userId = builder.userId;
        members = builder.members;
        appId = builder.appId;
        orgId = builder.orgId;
    }


    public UUID getOrgId() {
        return orgId;
    }

    public UUID getAppId() {
        return appId;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<UUID> getMembers() {
        return members;
    } // users in groups

    public UUID getDeviceId() {
        return deviceId;
    }

    public Query getQuery() {
        return query;
    }

    public String getStream() {
        return query.getStream();
    }

    public ImmutableList<Arg> getArgs() {
        return query.getArgs();
    }

    public static final class Builder {

        private Query query;
        private UUID deviceId;
        private UUID userId;
        private UUID appId;
        private UUID orgId;
        private List<UUID> members;

        public Builder() {
        }

        public Builder(QueryWithIds copy) {
            query = copy.query;
            deviceId = copy.deviceId;
            userId = copy.userId;
            appId = copy.appId;
            orgId = copy.orgId;
            members = copy.members;
        }

        public Builder query(Query query) {
            this.query = query;
            return this;
        }

        public Builder deviceId(UUID deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder members(List<UUID> members) {
            this.members = members;
            return this;
        }

        public Builder appId(UUID appId) {
            this.appId = appId;
            return this;
        }

        public Builder orgId(UUID orgId) {
            this.orgId = orgId;
            return this;
        }


        public QueryWithIds build() {
            return new QueryWithIds(this);
        }
    }


}
