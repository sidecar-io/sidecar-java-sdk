package io.sidecar.access;


import com.fasterxml.jackson.annotation.JsonProperty;

class AccessKeyMixin {

    @SuppressWarnings("unused")
        //Used by Jackson
    AccessKeyMixin(@JsonProperty("keyId") String keyId,
                   @JsonProperty("secret") String secret) {
    }
}
