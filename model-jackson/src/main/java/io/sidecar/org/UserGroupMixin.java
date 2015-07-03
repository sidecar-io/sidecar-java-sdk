package io.sidecar.org;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class UserGroupMixin {

    @SuppressWarnings("unused") //Used by Jackson
    @JsonCreator
    UserGroupMixin(@JsonProperty("id") UUID id,
                   @JsonProperty("appId") UUID appId,
                   @JsonProperty("name") String name,
                   @JsonProperty("metadata") Map<String,String> metadata,
                   @JsonProperty("members") List<UserGroupMember> members,
                   @JsonProperty("deviceIds") List<UUID> deviceIds) {
    }
}
