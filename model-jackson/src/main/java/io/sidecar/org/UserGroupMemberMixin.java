package io.sidecar.org;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

abstract class UserGroupMemberMixin {

    @SuppressWarnings("unused")
        //Used by Jackson
    UserGroupMemberMixin(@JsonProperty("id") UUID userId,
                         @JsonProperty("role") UserGroupRole role) {
    }
}
