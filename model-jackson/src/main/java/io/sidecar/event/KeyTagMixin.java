package io.sidecar.event;


import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused") //Used by Jackson
abstract class KeyTagMixin {

    private KeyTagMixin(@JsonProperty("key") String key,
                         @JsonProperty("tags") Set<String> tags) {
    }

}

