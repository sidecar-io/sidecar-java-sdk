package io.sidecar.org;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused") //Used by Jackson
abstract class OrganizationMixin {

    private OrganizationMixin(@JsonProperty("name") String name,
                              @JsonProperty("url") String url,
                              @JsonProperty("type") OrganizationType type
    ) {
    }

}