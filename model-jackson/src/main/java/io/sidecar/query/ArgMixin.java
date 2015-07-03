package io.sidecar.query;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused") //Used by Jackson
abstract class ArgMixin {

    private ArgMixin(@JsonProperty("key") String key,
                     @JsonProperty("value") String value) {
    }


}
