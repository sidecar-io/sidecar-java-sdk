package io.sidecar.org;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused") //Used by Jackson
abstract class CredentialMixin {

    private CredentialMixin(@JsonProperty("username") String username,
                            @JsonProperty("password") String password) {
    }

    @JsonGetter("username")
    abstract String getUsername();

    @JsonGetter("password")
    abstract String getPassword();
}
