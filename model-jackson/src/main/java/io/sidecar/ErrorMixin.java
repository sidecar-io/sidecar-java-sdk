package io.sidecar;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused") //Used by Jackson
abstract class ErrorMixin {

    private ErrorMixin(@JsonProperty("status") String status,
                       @JsonProperty("message") String message) {
    }

    @JsonGetter("status")
    abstract String getStatus();

    @JsonGetter("message")
    abstract String getMessage();
}
