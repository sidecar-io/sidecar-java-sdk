package io.sidecar.query;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused") //Used by Jackson
abstract class QueryMixin {

    private QueryMixin(@JsonProperty("stream") String stream,
                       @JsonProperty("args") List<Arg> args) {
    }

    @JsonGetter("stream")
    abstract String getStreamName();

}
