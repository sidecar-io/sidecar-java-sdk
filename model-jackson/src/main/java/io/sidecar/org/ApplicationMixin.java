package io.sidecar.org;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused") //Used by Jackson
abstract class ApplicationMixin {


    private ApplicationMixin(@JsonProperty("orgId") UUID orgId,
                             @JsonProperty("name") String name
    ) {
    }

}