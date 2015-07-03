package io.sidecar.org;

import com.fasterxml.jackson.annotation.JsonProperty;

abstract class PlatformDeviceTokenMixin {

    @SuppressWarnings("unused")
        //Used by Jackson
    PlatformDeviceTokenMixin(@JsonProperty("platform") String platform,
                         @JsonProperty("deviceToken") String deviceToken) {
    }
}
