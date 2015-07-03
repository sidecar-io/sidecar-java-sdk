package io.sidecar.org;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused") //Used by Jackson
abstract class ApplicationMixin {


    private ApplicationMixin(@JsonProperty("orgId") UUID orgId,
                             @JsonProperty("name") String name,
                             @JsonProperty("description") String description
    ) {
    }

}